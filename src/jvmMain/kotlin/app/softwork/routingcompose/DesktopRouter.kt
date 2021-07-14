package app.softwork.routingcompose

import androidx.compose.runtime.*

public class DesktopRouter(initRoute: String) : AbstractRouter("") {
    internal val stack = mutableListOf(initRoute)

    override fun navigate(to: String) {
        require(to.startsWith("/"))
        update(newPath = to)
        stack.add(to)
    }

    public fun navigateBack() {
        stack.removeAt(stack.lastIndex)
        update(newPath = stack.last())
    }

    public companion object {
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
