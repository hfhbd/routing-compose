package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*

@Stable
public object HashRouter : Router {
    private var subCounter = 0
    private val subscriber: MutableMap<Int, (String) -> Unit> = mutableMapOf()

    private fun subscribe(block: (String) -> Unit): Int {
        subscriber[subCounter] = block
        return subCounter.also {
            subCounter += 1
        }
    }

    private fun removeSubscription(id: Int) {
        subscriber.remove(id)
    }

    override fun navigate(to: String) {
        subscriber.entries.forEach { (_, fn) ->
            fn(to)
        }
        window.history.pushState(null, title = "", "#$to")
    }

    @Composable
    override fun getPath(): State<String> {
        val defaultPath = window.location.hash.removePrefix("#")
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

