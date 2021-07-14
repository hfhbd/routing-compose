package app.softwork.routingcompose

import androidx.compose.runtime.*

/**
 * [Node] holding a `@Composable` block, which will be displayed, when a route matches this [Node].
 */
public abstract class ContentNode : Node() {
    @Composable
    public abstract fun display(argument: String)
}