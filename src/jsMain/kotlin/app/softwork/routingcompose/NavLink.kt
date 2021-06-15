package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.*

@Composable
public fun NavLink(
    to: String,
    attrs: AttrsBuilder<HTMLAnchorElement>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    A(attrs = {
        attrs()
        onClick {
            HashRouter.navigate(to)
        }
    }) { content() }
}