package app.softwork.routingcompose

import androidx.compose.runtime.*

public interface Router {
    @Composable
    public operator fun invoke(initRoute: String, builder: @Composable NavBuilder.() -> Unit): Node {
        val root = RootNode()
        NavBuilder(root).apply { builder() }
        val fullPath by getPath(initRoute)
        val withTrailingSlash = if(fullPath.endsWith("/")) fullPath else "$fullPath/"
        root.execute(withTrailingSlash)
        return root
    }

    @Composable
    public fun getPath(initRoute: String): State<String>

    public fun navigate(to: String)
}