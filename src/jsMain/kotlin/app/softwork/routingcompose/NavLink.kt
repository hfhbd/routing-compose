package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.*

/**
 * Routing navigation Composable that will navigate to the provided path leveraging
 * the top-most Router implementation.
 * The Boolean parameter of [attrs] is true, if the path of the [current][Router.current]  starts with [to].
 */
@Composable
public fun NavLink(
    to: String,
    attrs: (AttrsScope<HTMLAnchorElement>.(Boolean) -> Unit)? = null,
    content: ContentBuilder<HTMLAnchorElement>? = null
) {
    val router = Router.current
    A(
        href = to,
        attrs = {
            val currentPath = router.currentPath.path
            val selected = if (to == "/") {
                currentPath == to
            } else {
                currentPath.startsWith(to)
            }

            onClick {
                if (it.ctrlKey || it.metaKey) return@onClick
                router.navigate(to)
                it.preventDefault()
            }
            attrs?.invoke(this, selected)
        },
        content = content
    )
}
