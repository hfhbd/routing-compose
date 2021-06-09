package app.softwork.routingcompose

import androidx.compose.runtime.*

public sealed class Node {
    public abstract fun matches(subRoute: String): Boolean


    @Composable
    public abstract fun execute(path: String)
}
