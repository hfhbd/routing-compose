package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlin.reflect.*

/**
 * Provide the router implementation through a CompositionLocal so deeper level
 * Composables in the composition can have access to the current router.
 *
 * This is particularly useful for [NavLink], so we can have a single Composable
 * agnostic of the top level router implementation.
 *
 * To use this composition, you need to invoke any [Router] implementation first.
 */
@Deprecated("This will be internal again in the next release. Use val router by Router instead. Please create an issue, why you still need this.")
public val RouterCompositionLocal: ProvidableCompositionLocal<Router> =
    staticCompositionLocalOf { error("Router not defined, cannot provide through RouterCompositionLocal.") }

public interface Router {

    @Composable
    public operator fun invoke(
        initRoute: String,
        builder: NavBuilder.() -> Unit
    ): Node {
        val root = RootNode()

        // Provide [RouterCompositionLocal] to composables deeper in the composition.
        CompositionLocalProvider(
            RouterCompositionLocal provides this@Router
        ) {
            NavBuilder(root).builder()

            val fullPath by getPath(initRoute)
            val withTrailingSlash = if (fullPath.endsWith("/")) fullPath else "$fullPath/"
            root.execute(withTrailingSlash)
        }
        return root
    }

    @Composable
    public fun getPath(initRoute: String): State<String>

    public fun navigate(to: String)

    public companion object {

        @Composable
        public operator fun getValue(ref: Any?, property: KProperty<*>): Router {
            return RouterCompositionLocal.current
        }
    }
}
