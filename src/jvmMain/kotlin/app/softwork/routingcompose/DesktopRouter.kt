package app.softwork.routingcompose

import androidx.compose.runtime.*

public class DesktopRouter private constructor(initRoute: String) : Router("") {
    private val stack = mutableListOf(initRoute)

    override fun navigate(to: String) {
        require(to.startsWith("/"))
        update(newPath = to)
        stack.add(to)
    }

    internal fun navigateBack() {
        stack.removeAt(stack.lastIndex)
        update(newPath = stack.last())
    }

    public companion object {
        /**
         * Creates a new [DesktopRouter] with the initial route [initRoute] and the route builder [navBuilder].
         *
         * You should never have more than 1 [DesktopRouter] per [Window][androidx.compose.desktop.Window].
         * To get the current [Router] inside the [navBuilder] `@Composable` tree call [Router.current].
         */
        @Composable
        public operator fun invoke(initRoute: String, navBuilder: NavBuilder.() -> Unit) {
            DesktopRouter(initRoute).invoke(initRoute, navBuilder)
        }
    }
}

public fun Router.navigateBack() {
    val router = this as DesktopRouter
    router.navigateBack()
}
