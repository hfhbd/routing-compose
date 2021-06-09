package app.softwork.routingcompose

import androidx.compose.runtime.*

public class SimpleContentNode : ContentNode() {
    public lateinit var content: @Composable () -> Unit

    override fun matches(subRoute: String): Boolean = true

    @Composable
    override fun display(subRoute: String) {
        content()
    }

    override fun toString(): String = "SimpleContentNode()"
}