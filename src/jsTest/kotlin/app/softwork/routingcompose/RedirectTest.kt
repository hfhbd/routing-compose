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

    @Test
    fun redirectingNoMatchTest() = runTest {
        val router = MockRouter()
        composition {
            router("/bar") {
                route("foo") {
                    Text("foo")
                }
                route("bar") {
                    Text("bar")
                }
                noMatch {
                    redirect("foo", hide = true)
                }
            }
        }
        assertEquals("bar", root.innerHTML)

        router.navigate("/asdf")
        waitForChanges()
        waitForChanges()
        assertEquals("foo", root.innerHTML)
    }
}
