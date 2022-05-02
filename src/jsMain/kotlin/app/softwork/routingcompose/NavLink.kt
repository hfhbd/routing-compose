package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.*

/**
 * Routing navigation Composable that will navigate to the provided path leveraging
 * the top-most Router implementation.
 */
@Composable
public fun NavLink(
    to: String,
    attrs: (AttrsScope<HTMLAnchorElement>.() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val router = Router.current
    A(
        href = to,
        attrs = {
            attrs?.invoke(this)
            onClick {
                router.navigate(to)
                it.preventDefault()
            }
        }
    ) { content() }
}
