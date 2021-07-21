package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlin.reflect.*


public abstract class Router(private val initPath: String) {
    public abstract fun navigate(to: String)


    private var subCounter = 0
    private val subscriber: MutableMap<Int, (String) -> Unit> = mutableMapOf()

    private fun subscribe(block: (String) -> Unit): Int {
        subscriber[subCounter] = block
        return subCounter.also {
            subCounter += 1
        }
    }

    private fun removeSubscription(id: Int) {
        subscriber.remove(id)
    }

    internal fun update(newPath: String) {
        subscriber.entries.forEach { (_, fn) ->
            fn(newPath)
        }
    }

    @Composable
    internal fun getPath(initRoute: String): State<String> {
        require(initRoute.startsWith("/")) { "initRoute must start with a slash." }
        val defaultPath = initPath.ifBlank { initRoute }
        val path = remember { mutableStateOf(defaultPath) }
        DisposableEffect(Unit) {
            val id = subscribe {
                path.value = it
            }
            onDispose {
                removeSubscription(id)
            }
        }
        return path
    }

    @Composable
    public operator fun invoke(
        initRoute: String,
        builder: NavBuilder.() -> Unit
    ) {
        val root = RootNode()

        // Provide [RouterCompositionLocal] to composables deeper in the composition.
        CompositionLocalProvider(
            RouterCompositionLocal provides this
        ) {
            NavBuilder(root).builder()

            val fullPath by getPath(initRoute)
            val withTrailingSlash = if (fullPath.endsWith("/")) fullPath else "$fullPath/"
            root.execute(withTrailingSlash)
        }
    }

    public companion object {
        private val RouterCompositionLocal: ProvidableCompositionLocal<Router> =
            staticCompositionLocalOf { error("Router not defined, cannot provide through RouterCompositionLocal.") }

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
