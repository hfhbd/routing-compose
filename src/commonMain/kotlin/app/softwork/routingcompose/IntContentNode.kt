package app.softwork.routingcompose

import androidx.compose.runtime.*

public class IntContentNode(public val content: @Composable NavBuilder.Content.(Int) -> Unit) : ContentNode() {
    override fun matches(subRoute: String): Boolean = subRoute.toIntOrNull() != null

    @Composable
    override fun display(argument: String) {
        NavBuilder.Content.content(argument.toInt())
    }

    override fun toString(): String = "IntContentNode()"
}
