package app.softwork.routingcompose

import androidx.compose.runtime.*

class MockRouter : Router {
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
        require(to.startsWith("/"))
        subscriber.entries.forEach { (_, fn) ->
            fn(to)
        }
        currentPath = "#$to"
    }

    private var currentPath = "#"

    @Composable
    override fun getPath(initRoute: String): State<String> {
        require(initRoute.startsWith("/"))
        val defaultPath = currentPath.removePrefix("#").ifBlank { initRoute }
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