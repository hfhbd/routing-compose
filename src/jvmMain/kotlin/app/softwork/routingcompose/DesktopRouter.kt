package app.softwork.routingcompose

import androidx.compose.runtime.*

public class DesktopRouter private constructor() : Router() {
    private val stack = mutableStateListOf<String>()

    @Composable
    override fun getPath(initPath: String): State<String> {
        return derivedStateOf { stack.lastOrNull() ?: initPath }
    }

    override fun navigate(to: String) {
        stack.add(to)
    }

    internal fun navigateBack() {
        stack.removeAt(stack.lastIndex)
    }

    public companion object {
        /**
         * Creates a new [DesktopRouter] with the initial route [initRoute] and the route builder [navBuilder].
         *
         * You should never have more than 1 [DesktopRouter] per [Window][androidx.compose.desktop.Window].
         * To get the current [Router] inside the [navBuilder] `@Composable` tree call [Router.current].
         */
        @Routing
        @Composable
        public operator fun invoke(initRoute: String, navBuilder: @Composable NavBuilder.() -> Unit) {
            DesktopRouter().invoke(initRoute, navBuilder)
        }
    }
}

public fun Router.navigateBack() {
    val router = this as DesktopRouter
    router.navigateBack()
}
