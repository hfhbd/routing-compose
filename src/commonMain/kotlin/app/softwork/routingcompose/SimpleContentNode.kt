package app.softwork.routingcompose

import androidx.compose.runtime.*

public class SimpleContentNode : ContentNode() {
    public lateinit var content: @Composable NavBuilder.Content.() -> Unit

    override fun matches(subRoute: String): Boolean = true

    @Composable
    override fun display(argument: String) {
        NavBuilder.Content.content()
    }

    override fun toString(): String = "SimpleContentNode()"
}
