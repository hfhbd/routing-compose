package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

public class UUIDContentNode : ContentNode() {
    public lateinit var content: @Composable (UUID) -> Unit

    override fun matches(subRoute: String): Boolean = UUID.isValidUUIDString(subRoute)

    @Composable
    override fun display(path: String) {
        val uuid = path.toUUID()
        content(uuid)
    }
}