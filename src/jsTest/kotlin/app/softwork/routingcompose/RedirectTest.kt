package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.testutils.*
import kotlin.test.*

@ComposeWebExperimentalTestsApi
class RedirectTest {
    @Test
    fun redirectingTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route("foo") {
                    noMatch {
                        Text("foo")
                    }
                }
                redirect("bar", "baz", target = "foo")
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)

        router.navigate("/bar")
        waitForChanges()
        assertEquals("foo", root.innerHTML, "First")

        router.navigate("/baz")
        waitForChanges()
        waitForChanges()
        assertEquals("foo", root.innerHTML, "Second")
    }
}
