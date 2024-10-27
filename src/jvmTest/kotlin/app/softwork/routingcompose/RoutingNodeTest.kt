package app.softwork.routingcompose

import androidx.compose.ui.test.junit4.*
import kotlinx.coroutines.*
import org.junit.*
import kotlin.test.*
import kotlin.test.Test

class RoutingNodeTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun intRoutingNode() = runBlocking(Dispatchers.Main) {
        val router: Router = DesktopRouter()
        var result: String? = null
        rule.setContent {
            router("/") {
                int { i ->
                    result = "$i"
                }
                noMatch {
                    result = "First"
                }
            }
        }
        rule.awaitIdle()
        assertEquals("First", result)

        router.navigate("/42")
        rule.awaitIdle()
        assertEquals("42", result)

        router.navigateBack()
        rule.awaitIdle()
        assertEquals("First", result)
    }

    @Test
    fun intNestedRoutingNode() = runBlocking(Dispatchers.Main) {
        val router: Router = DesktopRouter()
        var result: String? = null
        rule.setContent {
            router("/") {
                int { user ->
                    result = "$user"
                    int { i ->
                        result += "$i"
                    }
                }
                noMatch {
                    result = "First"
                }
            }
        }
        rule.awaitIdle()
        router.navigate("/42/5")
        rule.awaitIdle()
        assertEquals("425", result)
    }
}
