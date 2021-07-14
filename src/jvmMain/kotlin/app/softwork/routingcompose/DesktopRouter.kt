package app.softwork.routingcompose

import androidx.compose.runtime.*

public class DesktopRouter : AbstractRouter("") {
    override fun navigate(to: String) {
        require(to.startsWith("/"))
        update(newPath = to)
    }

    public companion object {
        @Composable
        public operator fun invoke(initRoute: String, navBuilder: NavBuilder.() -> Unit) {
            DesktopRouter().invoke(initRoute, navBuilder)
        }
    }
}
