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
public class NavBuilder internal constructor(
    private val remainingPath: Path
) {
    public val parameters: Parameters? get() = remainingPath.parameters

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
        nestedRoute: @Composable NavBuilder.() -> Unit
    ) {
        val relaxedRoute = route.removePrefix("/").removeSuffix("/")
        require(!relaxedRoute.contains("/")) { "To use nested routes, use route() { route() { } } instead." }
        val currentPath = remainingPath.currentPath
        if ((match == Match.NoMatch || match == Match.Constant) && relaxedRoute == currentPath) {
            val newPath = remainingPath.newPath(currentPath)
            val newState = remember(newPath) { NavBuilder(newPath) }
            newState.nestedRoute()
            match = Match.Constant
        }
    }

    /**
     * Executes its children when the requested subroute is a [String].
     */
    @Routing
    @Composable
    public fun string(nestedRoute: @Composable NavBuilder.(String) -> Unit) {
        val currentPath = remainingPath.currentPath
        if ((match == Match.NoMatch || match == Match.String) && currentPath.isNotEmpty()) {
            val newPath = remainingPath.newPath(currentPath)
            val newState = remember(newPath) { NavBuilder(newPath) }
            newState.nestedRoute(currentPath)
            match = Match.String
        }
    }

    /**
     * Executes its children when the requested subroute is a [Int].
     */
    @Routing
    @Composable
    public fun int(nestedRoute: @Composable NavBuilder.(Int) -> Unit) {
        val currentPath = remainingPath.currentPath
        val int = currentPath.toIntOrNull()
        if ((match == Match.NoMatch || match == Match.Integer) && int != null) {
            val newPath = remainingPath.newPath(currentPath)
            val newState = remember(newPath) { NavBuilder(newPath) }
            newState.nestedRoute(int)
            match = Match.Integer
        }
    }

    /**
     * Executes its children when the requested subroute is a [UUID].
     */
    @Routing
    @Composable
    public fun uuid(nestedRoute: @Composable NavBuilder.(UUID) -> Unit) {
        val currentPath = remainingPath.currentPath
        val uuid = currentPath.toUUIDOrNull()
        if ((match == Match.NoMatch || match == Match.Uuid) && uuid != null) {
            val newPath = remainingPath.newPath(currentPath)
            val newState = remember(newPath) { NavBuilder(newPath) }
            newState.nestedRoute(uuid)
            match = Match.Uuid
        }
    }

    /**
     * Fallback if no matching route is found.
     */
    @Routing
    @Composable
    public fun noMatch(content: @Composable () -> Unit) {
        if (match == Match.NoMatch) {
            content()
        }
    }
}
