package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class ContentNode : Node() {
    @Composable
    override fun execute(subRoute: String) {
        display(subRoute)
    }

    @Composable
    public abstract fun display(path: String)
}