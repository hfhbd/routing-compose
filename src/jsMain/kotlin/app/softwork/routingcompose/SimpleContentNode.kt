package app.softwork.routingcompose

import androidx.compose.runtime.*

public class SimpleContentNode(public val id: String) : ContentNode() {
    public lateinit var content: @Composable () -> Unit

    override fun matches(subRoute: String): Boolean {
        println("matches $this $subRoute\nAAAA")
        return true
    }

    @Composable
    override fun display(subRoute: String) {
        println("display $this $subRoute\nAAAA")
        content()
    }

    override fun toString(): String {
        return "SimpleContentNode(id=$id)"
    }
}