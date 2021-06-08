package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import kotlin.test.*

internal class HashRouterTest {

    @Test
    fun simpleTest() = runTest {
        val router = MockRouter()
        compose {
            router {
                route("/foo") {
                    Text("Foo")
                }
                route("/bar") {
                    Text("bar")
                }
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("other", content)

        router.navigate("/foo")
        waitForChanges()
        assertEquals("foo", content)

        router.navigate("/bar")
        waitForChanges()
        assertEquals("bar", content)
    }

    @Test
    fun deepTest() = runTest {
        val router = MockRouter()
        compose {
            router {
                route("/foo") {
                    route("/bar") {
                        Text("bar")
                    }
                    noMatch {
                        Text("Foo")
                    }
                }
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("other", content)

        router.navigate("/foo")
        waitForChanges()
        assertEquals("foo", content)

        router.navigate("/foo/bar")
        waitForChanges()
        assertEquals("bar", content)
    }
}
