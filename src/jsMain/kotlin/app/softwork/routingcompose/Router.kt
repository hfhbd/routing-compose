package app.softwork.routingcompose

import androidx.compose.runtime.*

public interface Router {
    @Composable
    public operator fun invoke(builder: @Composable NavBuilder.() -> Unit) {
        val root = NavBuilder(RootNode()).apply { builder() }.build()
        val fullPath by getPath()

        root.execute(fullPath)
    }

    @Composable
    public fun getPath(): State<String>

    public fun navigate(to: String)
}