package app.softwork.routingcompose

import androidx.compose.runtime.*

/**
 * Creates a new [DesktopRouter] with the initial route [initRoute] and the route builder [navBuilder].
 *
 * To get the current [Router] inside the [navBuilder] `@Composable` tree call [Router.current].
 */
@Composable
public fun DesktopRouter(initRoute: String, navBuilder: @Composable RouteBuilder.() -> Unit) {
    DesktopRouter().invoke(initRoute, navBuilder)
}

internal class DesktopRouter : Router {
    override val currentPath: Path
        get() = Path.from(stack.last().path)

    private data class Entry(val path: String, val hide: Boolean)

    private val stack = mutableStateListOf<Entry>()

    @Composable
    override fun getPath(initPath: String): State<String> = remember {
        derivedStateOf {
            stack.lastOrNull {
                !it.hide
            }?.path ?: initPath
        }
    }

    override fun navigate(to: String, hide: Boolean) {
        stack.add(Entry(to, hide))
    }

    internal fun navigateBack() {
        stack.removeAt(stack.lastIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DesktopRouter) {
            return false
        }
        return stack == other.stack
    }

    override fun hashCode(): Int {
        return stack.hashCode()
    }
}

public fun Router.navigateBack() {
    if (this is DesktopRouter) {
        navigateBack()
    } else {
        (this as DelegateRouter).router.navigateBack()
    }
}
