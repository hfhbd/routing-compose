package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlin.jvm.*

public interface Router {
    /**
     * The current path
     */
    public val currentPath: Path

    /**
     * Navigate to a new path.
     * @param to The path to navigate to.
     * @param hide Whether to hide the current path.
     * @param replace Whether to replace the current path.
     */
    public fun navigate(to: String, hide: Boolean = false, replace: Boolean = false)

    @Composable
    public fun getPath(initPath: String): State<String>

    public companion object {
        /**
         * Provide the router implementation through a CompositionLocal so deeper level
         * Composables in the composition can have access to the current router.
         *
         * This is particularly useful for [NavLink], so we can have a single Composable
         * agnostic of the top level router implementation.
         *
         * To use this composition, you need to invoke any [Router] implementation first.
         */
        public val current: Router
            @Composable
            get() = RouterCompositionLocal.current
    }
}

internal val RouterCompositionLocal: ProvidableCompositionLocal<Router> =
    compositionLocalOf { error("Router not defined, cannot provide through RouterCompositionLocal.") }

@Composable
public fun Router.route(
    initRoute: String,
    routing: @Composable RouteBuilder.() -> Unit
) {
    CompositionLocalProvider(RouterCompositionLocal provides this) {
        val rawPath by getPath(initRoute)
        val path = Path.from(rawPath)
        val node = remember(path) { RouteBuilder(path.path, path) }
        node.routing()
    }
}

public fun Router.navigate(to: String, parameters: Parameters, hide: Boolean = false, replace: Boolean = false) {
    navigate("$to?$parameters", hide = hide, replace = replace)
}

@JvmName("navigateParameterList")
public fun Router.navigate(
    to: String,
    parameters: Map<String, List<String>>,
    hide: Boolean = false,
    replace: Boolean = false
) {
    navigate(to, Parameters.from(parameters), hide = hide, replace = replace)
}

public fun Router.navigate(
    to: String,
    parameters: Map<String, String>,
    hide: Boolean = false,
    replace: Boolean = false
) {
    navigate(to, Parameters.from(parameters), hide = hide, replace = replace)
}
