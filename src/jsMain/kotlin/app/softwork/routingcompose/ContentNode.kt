package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class ContentNode : Node() {
    @Composable
    public abstract fun display(subRoute: String)
}