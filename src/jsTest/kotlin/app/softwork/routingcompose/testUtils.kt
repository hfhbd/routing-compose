package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*
import kotlinx.coroutines.*
import kotlinx.uuid.*
import org.jetbrains.compose.web.*
import org.w3c.dom.*
import kotlin.coroutines.*
import kotlin.test.*

fun runTest(block: suspend Testing.() -> Unit) {
    val scope = MainScope()
    scope.launch {
        val testing = Testing().apply { block() }
        testing.reset()
    }
}

private object MutationObserverOptions : MutationObserverInit {
    override var childList: Boolean? = true
    override var attributes: Boolean? = true
    override var characterData: Boolean? = true
    override var subtree: Boolean? = true
    override var attributeOldValue: Boolean? = true
}

class Testing {
    private val testElement = document.createElement("testElement${UUID().toString().replace("-", "")}")

    fun compose(block: @Composable () -> Unit) {
        renderComposable(root = testElement) {
            block()
        }
    }

    val content get() = testElement.innerHTML

    suspend fun waitForChanges(): Unit = suspendCoroutine { continuation ->
        val observer = MutationObserver { _, observer ->
            continuation.resume(Unit)
            observer.disconnect()
        }
        observer.observe(testElement, MutationObserverOptions)
    }

    fun reset() {
        document.removeChild(testElement)
    }

    suspend fun MockRouter.navigate(to: String, should: String) {
        navigate(to)
        waitForChanges()
        assertEquals(should, content)
    }
}
