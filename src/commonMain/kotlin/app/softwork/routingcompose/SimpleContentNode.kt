package app.softwork.routingcompose

import androidx.compose.runtime.*

public class SimpleContentNode(public val content: @Composable NavBuilder.Content.() -> Unit) : ContentNode() {

    override fun matches(subRoute: String): Boolean = true

    @Composable
    override fun display(argument: String) {
        NavBuilder.Content.content()
    }

    override fun toString(): String = "SimpleContentNode()"
}
