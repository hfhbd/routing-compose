package app.softwork.routingcompose


import androidx.compose.runtime.*
import kotlinx.browser.*

/**
 * A router leveraging the History API (https://developer.mozilla.org/en-US/docs/Web/API/History).
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
