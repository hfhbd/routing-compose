package app.softwork.routingcompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.serializer
import kotlinx.uuid.UUID
import kotlinx.uuid.toUUIDOrNull

/**
 * Use the DSL functions to build the expected route handled by a [Router].
 * If two routes match the same path, the first declared route is chosen.
 */
@Routing
public class RouteBuilder internal constructor(
    private val basePath: String,
    private val remainingPath: Path
) {
    override fun toString(): String {
        return "$basePath $remainingPath ${hashCode()}"
    }

    public val parameters: Parameters? = remainingPath.parameters

    private var content: (@Composable RouteBuilder.() -> Unit)? = null
    private var noMatch: (@Composable RouteBuilder.() -> Unit)? = null

    /**
     * Executes its children when the requested subroute matches one of these constant [route].
     *
     * To match `foo/bar`, create a [route] inside the first [route].
     */
    @Routing
    @Composable
    public fun route(
        vararg route: String,
        nestedRoute: @Composable RouteBuilder.() -> Unit
    ) {
        val relaxedRoute = route.check()
        val currentPath = remainingPath.currentPath
        println("CONSTANT CHECK $content $currentPath $relaxedRoute\n")
        if (content == null && currentPath in relaxedRoute) {
            println("CONSTANT FOUND $content $currentPath $relaxedRoute\n")
            execute(currentPath, currentPath, nestedRoute)
        }
    }

    @Routing
    @Composable
    public inline fun <reified T : Any> route(
        noinline noMatch: @Composable NoMatch.() -> Unit = {},
        noinline nestedRoute: @Composable RouteBuilder.(T) -> Unit
    ) {
        route(serializer<T>(), noMatch, nestedRoute)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Routing
    @Composable
    public fun <T : Any> route(
        serializer: DeserializationStrategy<T>,
        noMatch: @Composable NoMatch.() -> Unit = {},
        nestedRoute: @Composable RouteBuilder.(T) -> Unit
    ) {
        println(noMatch)
        val route = serializer.descriptor.routeAnnotation
            ?: error("${serializer.descriptor} does not have an @Route annotation.")
        val routes = route.route.split("/").drop(
            if (route.route[0] == '/') 1 else 0
        )
        println("${routes}\n")
        val parameters = arrayOfNulls<Any?>(size = serializer.descriptor.elementsCount)
        var currentBuilder = this
        val createRoute = @Composable {
            val serializerModule = Router.current.serializersModule
            serializer.deserialize(RoutingDecoder(serializerModule, parameters))
        }
        for ((index, path) in routes.withIndex()) {
            println("$index $path\n")
            if (path[0] == ':') {
                val elementIndex = serializer.descriptor.getElementIndex(path.drop(1))
                val pathDescriptor = serializer.descriptor.getElementDescriptor(elementIndex)

                val action: @Composable RouteBuilder.(parameter: Any) -> Unit = @Composable {
                    println("CALLED WITH $elementIndex and $it\n")
                    parameters[elementIndex] = it
                    if (index == routes.lastIndex) {
                        println("CREATEROUTE in action $path $index $routes\n")
                        val t = createRoute()
                        nestedRoute(t)
                    } else {
                        currentBuilder = this
                    }
                    // noMatch(noMatch)
                }

                when (pathDescriptor) {
                    Int.serializer().descriptor -> currentBuilder.int(action)
                    String.serializer().descriptor -> currentBuilder.string(action)
                    UUID.serializer().descriptor -> currentBuilder.uuid(action)
                }
            } else {
                currentBuilder.route(path) {
                    if (index == routes.lastIndex) {
                        println("CREATEROUTE $path $index $routes\n")
                        val t = createRoute()
                        nestedRoute(t)
                    } else {
                        currentBuilder = this
                    }
                    // noMatch(noMatch)
                }
            }
        }
    }

    @Routing
    @Composable
    public fun redirect(vararg route: String, target: String, hide: Boolean = false) {
        val routes = route.check()
        val currentPath = remainingPath.currentPath
        if (content == null && currentPath in routes) {
            val router = Router.current
            LaunchedEffect(Unit) {
                router.navigate(target, hide)
            }
        }
    }

    @Composable
    private fun execute(currentPath: String, debugPath: String, nestedRoute: @Composable RouteBuilder.() -> Unit) {
        val newPath = remainingPath.newPath(currentPath)
        val currentRouter = Router.current
        val delegatingRouter = remember(newPath) { DelegateRouter(basePath, debugPath, currentRouter) }
        CompositionLocalProvider(RouterCompositionLocal provides delegatingRouter) {
            println("Executing $content $currentPath $debugPath\n")
            val newState = RouteBuilder(basePath, newPath)
            content = {
                newState.nestedRoute()
            }
            show()
        }
    }

    @Composable
    internal fun show() {
        val content = content
        val noMatch = noMatch
        if (content != null) {
            println("Showing Content $this $content\n")
            content()
        } else if (noMatch != null) {
            println("Showing noMatch $this $content\n")
            noMatch()
        } else {
            println("Showing nothing $this")
        }
    }

    /**
     * Executes its children when the requested subroute is a non-empty [String].
     */
    @Routing
    @Composable
    public fun string(nestedRoute: @Composable RouteBuilder.(String) -> Unit) {
        val currentPath = remainingPath.currentPath
        if (content == null && currentPath.isNotEmpty()) {
            execute(currentPath, ":string") {
                nestedRoute(currentPath)
            }
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
        if (content == null && int != null) {
            execute(currentPath, ":int") {
                nestedRoute(int)
            }
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
        if (content == null && uuid != null) {
            execute(currentPath, ":uuid") {
                nestedRoute(uuid)
            }
        }
    }

    /**
     * Fallback if no matching route is found.
     */
    @Routing
    @Composable
    public fun noMatch(content: @Composable NoMatch.() -> Unit) {
        // val r = Router.current
        // println("$r\n")
        // println("noMatch $match\n")

        if (noMatch == null) {
            println("ENTER NOMATCH setter\n")
            noMatch = {
                NoMatch(remainingPath.path, remainingPath.parameters).content()
            }
        }
    }

    @Routing
    public class NoMatch(public val remainingPath: String, public val parameters: Parameters?) {
        @Routing
        @Composable
        public fun redirect(target: String, hide: Boolean = false) {
            val router = Router.current
            LaunchedEffect(Unit) {
                router.navigate(target, hide)
            }
        }
    }
}

@ExperimentalSerializationApi
private val SerialDescriptor.routeAnnotation: Route?
    get() {
        for (anno in annotations) {
            if (anno is Route) {
                return anno
            }
        }
        return null
    }

private fun Array<out String>.check(): List<String> {
    val relaxedRoute = map { it.removePrefix("/").removeSuffix("/") }
    require(relaxedRoute.none { it.contains("/") }) { "To use nested routes, use route() { route() { } } instead." }
    return relaxedRoute
}
