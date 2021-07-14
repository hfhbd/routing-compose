package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class AbstractRouter(private val initPath: String) : Router {
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

    public fun update(newPath: String) {
        subscriber.entries.forEach { (_, fn) ->
            fn(newPath)
        }
    }

    @Composable
    override fun getPath(initRoute: String): State<String> {
        require(initRoute.startsWith("/")) { "initRoute must start with a slash." }
        val defaultPath = initPath.ifBlank { initRoute }
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
