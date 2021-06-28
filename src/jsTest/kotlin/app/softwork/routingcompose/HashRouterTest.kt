package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.*
import kotlin.test.*

internal class HashRouterTest {

    @Test
    fun simpleTest() = runTest {
        val router = MockRouter()
        compose {
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

        router.navigate("/foo", "foo")
        router.navigate("/bar", "bar")
    }

    @Test
    fun emptyTest() = runTest {
        val router = MockRouter()
        compose {
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
        compose {
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
        router.navigate("/foo/bar", "bar")
        router.navigate("/foo/bar/baz", "baz")
        router.navigate("/", "other")
    }

    @Test
    fun notFound() = runTest {
        val router = MockRouter()
        assertFailsWith<IllegalStateException> {
            compose {
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
            compose {
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
        compose {
            MockRouter().invoke("/") {
                route("foo") {
                    noMatch { Text("Foo") }
                }
                noMatch {
                    Text("NoMatch")

                    val router by Router
                    Input {
                        ref {
                            input = (it as HTMLInputElement)
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
        waitChanges()
        assertEquals("Foo", root.innerHTML)
    }
}
