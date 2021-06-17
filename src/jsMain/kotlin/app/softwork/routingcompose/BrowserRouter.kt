package app.softwork.routingcompose


import androidx.compose.runtime.*
import kotlinx.browser.*

/**
 * A router leveraging the History API (https://developer.mozilla.org/en-US/docs/Web/API/History).
 *
 * Using a BrowserRouter requires you to implement a catch-all to send the same resource for
 * every path the router intends to control. BrowserRouter will handle the proper child composition.
 *
 * Without a catch-all rule, will get a 404 or "Cannot GET /path" error each time you refresh or
 * request a specific path. Each server's implementation of a catch-all will be different and you
 * should handle this based on the webserver environment you're running.
 *
 * For more information about this catch-all, check your webserver implementation's specific
 * instructions. For development environments, see the RoutingCompose Readme
 * for full instructions.
 */
public object BrowserRouter : Router {
    private var subCounter = 0
    private val subscriber: MutableMap<Int, (String) -> Unit> = mutableMapOf()

    private fun subscribe(block: (String) -> Unit): Int {
        subscriber[subCounter] = block
        return subCounter.also {
            subCounter += 1
        }
    }

    init {
        window.onpopstate = {
            notifySubscribersOfNewPath()
        }
    }

    private fun notifySubscribersOfNewPath(newPath: String = window.location.pathname) {
        subscriber.entries.forEach { (_, fn) ->
            fn(newPath)
        }
    }

    private fun removeSubscription(id: Int) {
        subscriber.remove(id)
    }

    override fun navigate(to: String) {
        require(to.startsWith("/"))

        window.history.pushState(null, "", to)
        /*
        The history API unfortunately provides no callback to listen for
        [window.history.pushState], so we need to notify subscribers when pushing a new path.
         */
        notifySubscribersOfNewPath()
    }

    @Composable
    override fun getPath(initRoute: String): State<String> {
        require(initRoute.startsWith("/")) { "initRoute must start with a slash." }

        val defaultPath = window.location.pathname.ifBlank { initRoute }
        val path = remember { mutableStateOf(defaultPath) }
        DisposableEffect(Unit) {
            val id = subscribe {
                path.value = it
            }
            onDispose {
                removeSubscription(id)
            }
        }
        return path
    }
}
