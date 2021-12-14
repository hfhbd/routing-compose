package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.testutils.*
import org.w3c.dom.*
import kotlin.test.*

@ComposeWebExperimentalTestsApi
internal class RouterTest {

    @Test
    fun simpleTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route("foo") {
                    noMatch {
                        Text("foo")
                    }
                }
                route("bar") {
                    noMatch {
                        Text("bar")
                    }
                }
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)

        router.navigate("/foo")
        waitForRecompositionComplete()
        assertEquals("foo", root.innerHTML)
        router.navigate("/bar")
        waitForRecompositionComplete()
        assertEquals("bar", root.innerHTML)
    }

    @Test
    fun emptyTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)
    }

    @Test
    fun mixed() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                int {
                    Text("int $it")
                }
                string {
                    Text("string $it")
                }
                noMatch {
                    Text("noMatch")
                }
            }
        }
        assertEquals("noMatch", root.innerHTML)
        router.navigate("/42")
        waitForRecompositionComplete()
        assertEquals("int 42", root.innerHTML)
        router.navigate("/foo")
        waitForRecompositionComplete()
        assertEquals("string foo", root.innerHTML)
    }

    @Test
    fun deepTest() = runTest {
        val router = MockRouter()
        composition {
            router("/foo") {
                route("foo") {
                    route("bar") {
                        route("baz") {
                            noMatch {
                                Text("baz")
                            }
                        }
                        noMatch {
                            Text("bar")
                        }
                    }
                    noMatch {
                        Text("foo")
                    }
                }
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("foo", root.innerHTML)
        router.navigate("/foo/bar")
        waitForRecompositionComplete()
        assertEquals("bar", root.innerHTML)
        router.navigate("/foo/bar/baz")
        waitForRecompositionComplete()
        assertEquals("baz", root.innerHTML)
        router.navigate("/")
        waitForRecompositionComplete()
        assertEquals("other", root.innerHTML)
    }

    @Test
    fun nestedRoute() = runTest {
        val router = MockRouter()
        assertFailsWith<IllegalArgumentException> {
            composition {
                router("/") {
                    route("foo/foo") {
                        noMatch {
                            Text("FooBar")
                        }
                    }
                    noMatch {
                        Text("No match")
                    }
                }
            }
        }
    }

    @Test
    fun wrongDynamicTest() = runTest {
        val router = MockRouter()
        var addNewRoute by mutableStateOf(false)
        composition {
            router("/") {
                if (addNewRoute) {
                    int {
                        Text(it.toString())
                    }
                }
                int {
                    Text("wrong")
                }
                noMatch {
                    Text("NoMatch")
                }
            }
        }
        assertEquals("NoMatch", root.innerHTML)

        router.navigate("/1")
        waitForRecompositionComplete()
        assertEquals("wrong", root.innerHTML)

        addNewRoute = true
        router.navigate("/1")
        waitForRecompositionComplete()
        assertEquals("1wrong", root.innerHTML)
    }

    @Test
    fun correctDynamicTest() = runTest {
        val router = MockRouter()
        var addNewRoute by mutableStateOf(false)
        composition {
            router("/") {
                if (addNewRoute) {
                    int {
                        Text(it.toString())
                    }
                } else {
                    int {
                        Text("correct")
                    }
                }
                noMatch {
                    Text("NoMatch")
                }
            }
        }
        assertEquals("NoMatch", root.innerHTML)

        router.navigate("/1")
        waitForRecompositionComplete()
        assertEquals("correct", root.innerHTML)

        addNewRoute = true
        router.navigate("/1")
        waitForRecompositionComplete()
        assertEquals("1", root.innerHTML)
    }

    @Test
    fun RouterCompositionLocalTest() = runTest {
        val mockRouter = MockRouter()
        var input: HTMLInputElement? = null
        composition {
            mockRouter("/") {
                route("foo") {
                    Text("Foo")
                }
                noMatch {
                    Text("NoMatch")

                    val router = Router.current
                    Input(type = InputType.Text) {
                        ref {
                            input = it
                            onDispose {

                            }
                        }
                        onClick {
                            router.navigate(to = "/foo")
                        }
                    }
                }
            }
        }
        assertEquals("""NoMatch<input type="text">""", root.innerHTML)
        input!!.click()
        waitForRecompositionComplete()
        assertEquals("Foo", root.innerHTML)
    }

    @Test
    fun relaxedTest() = runTest {
        val router = MockRouter()
        composition {
            router("foo") {
                route("/foo") {
                    noMatch {
                        Text("foo")
                    }
                }
                route("bar") {
                    noMatch {
                        Text("bar")
                    }
                }
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("foo", root.innerHTML)

        router.navigate("/bar")
        waitForRecompositionComplete()
        assertEquals("bar", root.innerHTML)

        router.navigate("/foo")
        waitForRecompositionComplete()
        assertEquals("foo", root.innerHTML)

        router.navigate("/asf")
        waitForRecompositionComplete()
        assertEquals("other", root.innerHTML)
    }
}
