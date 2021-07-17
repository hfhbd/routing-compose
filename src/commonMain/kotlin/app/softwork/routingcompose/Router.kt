package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlin.reflect.*



public interface Router {

    @Composable
    public operator fun invoke(
        initRoute: String,
        builder: NavBuilder.() -> Unit
    ) {
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
    }

    @Composable
    public fun getPath(initRoute: String): State<String>

    public fun navigate(to: String)

    public companion object {
        private val RouterCompositionLocal: ProvidableCompositionLocal<Router> =
            staticCompositionLocalOf { error("Router not defined, cannot provide through RouterCompositionLocal.") }

        public val current: Router
            @Composable
            get() = RouterCompositionLocal.current
    }
}
