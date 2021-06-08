package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

public class StringContentNode : ContentNode() {
    public lateinit var content: @Composable (String) -> Unit

    override fun matches(subRoute: String): Boolean = true

    @Composable
    override fun display(path: String) {
        content(path)
    }
}