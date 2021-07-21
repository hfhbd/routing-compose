package app.softwork.routingcompose

import androidx.compose.runtime.*

public class IntContentNode : ContentNode() {
    public lateinit var content: @Composable NavBuilder.Content.(Int) -> Unit

    override fun matches(subRoute: String): Boolean = subRoute.toIntOrNull() != null

    @Composable
    override fun display(argument: String) {
        NavBuilder.Content.content(argument.toInt())
    }

    override fun toString(): String = "IntContentNode()"
}
