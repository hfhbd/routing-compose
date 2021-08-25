package app.softwork.routingcompose

import androidx.compose.runtime.*

public class StringContentNode(public val content: @Composable NavBuilder.Content.(String) -> Unit) : ContentNode() {

    override fun matches(subRoute: String): Boolean = subRoute.isNotEmpty()

    @Composable
    override fun display(argument: String) {
        NavBuilder.Content.content(argument)
    }
}
