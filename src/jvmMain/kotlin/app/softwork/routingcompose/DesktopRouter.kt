package app.softwork.routingcompose

import androidx.compose.runtime.*

/**
 * Creates a new [DesktopRouter] with the initial route [initRoute] and the route builder [navBuilder].
 *
 * To get the current [Router] inside the [navBuilder] `@Composable` tree call [Router.current].
 */
@Deprecated("Use org.jetbrains.androidx.navigation:navigation-compose for Desktop and WASM instead")
@Routing
@Composable
public fun DesktopRouter(initRoute: String, navBuilder: @Composable RouteBuilder.() -> Unit) {
    DesktopRouter().route(initRoute, navBuilder)
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

    override fun navigate(to: String, hide: Boolean, replace: Boolean) {
        if (replace) {
            stack.set(stack.lastIndex, Entry(to, hide))
        } else {
            stack.add(Entry(to, hide))
        }
    }

    internal fun navigateBack() {
        stack.removeAt(stack.lastIndex)
    }
}

public fun Router.navigateBack() {
    if (this is DesktopRouter) {
        navigateBack()
    } else {
        (this as DelegateRouter).router.navigateBack()
    }
}
