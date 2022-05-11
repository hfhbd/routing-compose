package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

/**
 * Use the DSL functions to build the expected route handled by a [Router].
 * If two routes matches the same path, the first declared route is chosen.
 *
 * With dynamic routing displaying will not stop if two routes of the same kind matches the current route:
 *
 * wrong usage:
 *
 *     if(true) {
 *       int {
 *         Text("Match")
 *       }
 *     }
 *     int {
 *       Text("Will be displayed too")
 *     }
 *
 * correct usage:
 *
 *     if(true) {
 *       int {
 *         Text("Match")
 *       }
 *     } else {
 *       int {
 *         Text("Won't be displayed")
 *       }
 *     }
 */
@Routing
public class RouteBuilder internal constructor(private val basePath: String, private val remainingPath: Path) {
    public val parameters: Parameters? = remainingPath.parameters

    private var match by mutableStateOf(Match.NoMatch)

    private enum class Match {
        Constant, Integer, String, Uuid, NoMatch
    }

    /**
     * Executes its children when the requested subroute matches this constant [route].
     *
     * To match `foo/bar`, create a [route] inside the first [route].
     */
    @Routing
    @Composable
    public fun route(
        route: String,
        nestedRoute: @Composable RouteBuilder.() -> Unit
    ) {
        val relaxedRoute = route.removePrefix("/").removeSuffix("/")
        require(!relaxedRoute.contains("/")) { "To use nested routes, use route() { route() { } } instead." }
        require(relaxedRoute.isNotEmpty()) { "Route $route cannot be empty. Use noMatch instead." }

        val currentPath = remainingPath.currentPath
        if ((match == Match.NoMatch || match == Match.Constant) && relaxedRoute == currentPath) {
            execute(currentPath, nestedRoute)
            match = Match.Constant
        }
    }

    @Composable
    private fun execute(currentPath: String, nestedRoute: @Composable RouteBuilder.() -> Unit) {
        val newPath = remainingPath.newPath(currentPath)
        val currentRouter = Router.current
        val delegatingRouter = remember(newPath) { DelegatingRouter(basePath, currentRouter) }
        CompositionLocalProvider(RouterCompositionLocal provides delegatingRouter) {
            val newState = RouteBuilder(basePath, newPath)
            newState.nestedRoute()
        }
    }

    /**
     * Executes its children when the requested subroute is a [String].
     */
    @Routing
    @Composable
    public fun string(nestedRoute: @Composable RouteBuilder.(String) -> Unit) {
        val currentPath = remainingPath.currentPath
        if ((match == Match.NoMatch || match == Match.String) && currentPath.isNotEmpty()) {
            execute(currentPath) {
                nestedRoute(currentPath)
            }
            match = Match.String
        }
    }

    /**
     * Executes its children when the requested subroute is a [Int].
     */
    @Routing
    @Composable
    public fun int(nestedRoute: @Composable RouteBuilder.(Int) -> Unit) {
        val currentPath = remainingPath.currentPath
        val int = currentPath.toIntOrNull()
        if ((match == Match.NoMatch || match == Match.Integer) && int != null) {
            execute(currentPath) {
                nestedRoute(int)
            }
            match = Match.Integer
        }
    }

    /**
     * Executes its children when the requested subroute is a [UUID].
     */
    @Routing
    @Composable
    public fun uuid(nestedRoute: @Composable RouteBuilder.(UUID) -> Unit) {
        val currentPath = remainingPath.currentPath
        val uuid = currentPath.toUUIDOrNull()
        if ((match == Match.NoMatch || match == Match.Uuid) && uuid != null) {
            execute(currentPath) {
                nestedRoute(uuid)
            }
            match = Match.Uuid
        }
    }

    /**
     * Fallback if no matching route is found.
     */
    @Routing
    @Composable
    public fun noMatch(content: @Composable NoMatch.() -> Unit) {
        if (match == Match.NoMatch) {
            NoMatch(remainingPath.path, remainingPath.parameters).content()
        }
    }

    public class NoMatch(public val remainingPath: String, public val parameters: Parameters?)
}

private class DelegatingRouter(val basePath: String, val router: Router) : Router by router {
    override fun navigate(to: String) {
        when {
            to.startsWith("/") -> {
                router.navigate(to)
            }
            basePath == "/" -> {
                router.navigate("/$to")
            }
            else -> {
                router.navigate("$basePath/$to")
            }
        }
    }
}