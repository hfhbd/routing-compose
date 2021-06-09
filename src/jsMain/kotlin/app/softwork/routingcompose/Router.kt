package app.softwork.routingcompose

import androidx.compose.runtime.*

public interface Router {
    @Composable
    public operator fun invoke(initPath: String, builder: @Composable NavBuilder.() -> Unit): Node {
        require(initPath.startsWith("/"))
        val root = NavBuilder(RootNode()).apply { builder() }.build()
        val fullPath by getPath(initPath)
        val withTrailingSlash = if(fullPath.endsWith("/")) fullPath else "$fullPath/"
        println("updated with $withTrailingSlash")
        root.execute(withTrailingSlash)
        return root
    }

    @Composable
    public fun getPath(initPath: String): State<String>

    public fun navigate(to: String)
}