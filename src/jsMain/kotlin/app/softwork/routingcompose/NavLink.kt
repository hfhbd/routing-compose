package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

/**
 * Routing navigation Composable that will navigate to the provided path leveraging
 * the top-most Router implementation.
 */
@Composable
public fun NavLink(
    to: String,
    attrs: AttrsBuilder<Tag.A>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    val router = RouterCompositionLocal.current
    A(attrs = {
        attrs()
        onClick {
            router.navigate(to)
        }
    }) { content() }
}