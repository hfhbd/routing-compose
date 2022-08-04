package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*
import org.w3c.dom.*

/**
 * A router leveraging the History API (https://developer.mozilla.org/en-US/docs/Web/API/History).
 *
 * Using a BrowserRouter requires you to implement a catch-all to send the same resource for
 * every path the router intends to control.
 * BrowserRouter will handle the proper child composition.
 *
 * Without a catch-all rule, will get a 404 or "Cannot GET /path" error each time you refresh or
 * request a specific path.
 * Each server's implementation of a catch-all will be different, and you
 * should handle this based on the webserver environment you're running.
 *
 * For more information about this catch-all, check your webserver implementation's specific
 * instructions.
 * For development environments, see the RoutingCompose Readme
 * for full instructions.
 */
@Composable
public fun BrowserRouter(
    initPath: String,
    routeBuilder: @Composable RouteBuilder.() -> Unit
) {
    BrowserRouter().route(initPath, routeBuilder)
}

internal class BrowserRouter : Router {
    override val currentPath: Path
        get() = Path.from(currentLocation.value)

    private val currentLocation: MutableState<String> = mutableStateOf(window.location.newPath())

    @Composable
    override fun getPath(initPath: String): State<String> {
        LaunchedEffect(Unit) {
            currentLocation.value = window.location.newPath().takeUnless { it == "/" } ?: initPath
            window.onpopstate = {
                currentLocation.value = window.location.newPath()
                Unit
            }
        }
        return currentLocation
    }

    private fun Location.newPath() = "$pathname$search"

    override fun navigate(to: String, hide: Boolean) {
        if (hide) {
            currentLocation.value = to
        } else {
            window.history.pushState(null, "", to)
            /*
                The history API unfortunately provides no callback to listen to
                [window.history.pushState], so we need to notify subscribers when pushing a new path.
                */
            currentLocation.value = window.location.newPath()
        }
    }
}
