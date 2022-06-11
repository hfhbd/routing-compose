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
    attrs: AttrBuilderContext<HTMLAnchorElement>? = null,
    content: ContentBuilder<HTMLAnchorElement>? = null
) {
    val router = Router.current
    A(
        href = to,
        attrs = {
            if (router.currentPath.path.startsWith(to)) {
                classes("active")
            }
            onClick {
                if (it.ctrlKey || it.metaKey) return@onClick
                router.navigate(to)
                it.preventDefault()
            }
            attrs?.invoke(this)
        },
        content = content
    )
}
