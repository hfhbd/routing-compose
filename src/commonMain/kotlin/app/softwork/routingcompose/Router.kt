package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlin.jvm.*

public interface Router {
    public fun navigate(to: String)

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
        val node by derivedStateOf { RouteBuilder(path.path, path) }
        node.routing()
    }
}

public fun Router.navigate(to: String, parameters: Parameters) {
    navigate("$to?$parameters")
}

@JvmName("navigateParameterList")
public fun Router.navigate(to: String, parameters: Map<String, List<String>>) {
    navigate(to, Parameters.from(parameters))
}

public fun Router.navigate(to: String, parameters: Map<String, String>) {
    navigate(to, Parameters.from(parameters))
}
