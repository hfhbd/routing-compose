package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*

public object HashRouter : Router {
    private var subCounter = 0
    private val subscriber: MutableMap<Int, (String) -> Unit> = mutableMapOf()

    private fun subscribe(block: (String) -> Unit): Int {
        subscriber[subCounter] = block
        return subCounter.also {
            subCounter += 1
        }
    }

    init {
        window.onhashchange = {
            val new: String = window.location.hash.removePrefix("#")
            subscriber.entries.forEach { (_, fn) ->
                fn(new)
            }
        }
    }

    private fun removeSubscription(id: Int) {
        subscriber.remove(id)
    }

    override fun navigate(to: String) {
        require(to.startsWith("/"))
        window.location.hash = to
    }

    @Composable
    override fun getPath(initRoute: String): State<String> {
        require(initRoute.startsWith("/")) { "initRoute must start with a slash." }
        val defaultPath = window.location.hash.removePrefix("#").ifBlank { initRoute }
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

