package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
public fun NavLink(
    to: String,
    attrs: AttrsBuilder<Tag.A>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    A(attrs = {
        attrs()
        onClick {
            HashRouter.navigate(to)
        }
    }) { content() }
}