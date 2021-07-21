package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

public class UUIDContentNode : ContentNode() {
    public lateinit var content: @Composable NavBuilder.Content.(UUID) -> Unit

    override fun matches(subRoute: String): Boolean = UUID.isValidUUIDString(subRoute)

    @Composable
    override fun display(argument: String) {
        val uuid = argument.toUUID()
        NavBuilder.Content.content(uuid)
    }
}
