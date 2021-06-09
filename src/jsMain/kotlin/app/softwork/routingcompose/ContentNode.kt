package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class ContentNode : Node() {
    @Composable
    override fun execute(path: String) {
        println("executing $this $path \nAAAA")
        display(path)
    }

    @Composable
    public abstract fun display(subRoute: String)
}