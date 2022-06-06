package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.*

/**
 * Routing navigation Composable that will navigate to the provided path leveraging
 * the top-most Router implementation.
 * This implementation will use the `active` class if the current route starts with this link.
 */
@Composable
public fun NavLink(
    to: String,
    attrs: (AttrsScope<HTMLAnchorElement>.() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val router = Router.current
    A(
        attrs = {
            attrs?.invoke(this)
            if (router.currentPath.startsWith(to.removePrefix("/"))) {
                classes("active")
            }
            onClick {
                router.navigate(to)
            }
        }
    ) { content() }
}
