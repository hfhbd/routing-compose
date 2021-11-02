package app.softwork.routingcompose

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
    fun notFound() = runTest {
        val router = MockRouter()
        assertFailsWith<IllegalStateException> {
            composition {
                router("/") {
                    route("foo") {
                        noMatch {
                            Text("Should not be reached")
                        }
                    }
                }
            }
        }
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
    fun RouterCompositionLocalTest() = runTest {
        var input: HTMLInputElement? = null
        composition {
            MockRouter().invoke("/") {
                route("foo") {
                    noMatch { Text("Foo") }
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
}
