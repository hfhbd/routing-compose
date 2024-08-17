package app.softwork.routingcompose

import androidx.compose.runtime.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.testutils.*
import org.w3c.dom.*
import kotlin.test.*
import kotlin.uuid.*

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
    fun updateTest() = runTest {
        val router = MockRouter()
        var foo by mutableStateOf(false)
        composition {
            router("/") {
                if (foo) {
                    route("foo") {
                        noMatch {
                            Text("foo")
                        }
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
        assertEquals("other", root.innerHTML)

        foo = true
        waitForRecompositionComplete()
        assertEquals("foo", root.innerHTML)
    }

    @Test
    fun emptyTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                noMatch {
                    Text("other")
                    Text(remainingPath)
                    val parameters = parameters
                    if (parameters != null) {
                        Text(parameters.raw)
                    }
                }
            }
        }
        assertEquals("other/", root.innerHTML)
        router.navigate("/foo")
        waitForRecompositionComplete()
        assertEquals("other/foo", root.innerHTML)

        router.navigate("/foo", Parameters.from("V" to "b"))
        waitForRecompositionComplete()
        assertEquals("other/fooV=b", root.innerHTML)
    }

    @Test
    fun blankRouteTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route("/") {
                    Text("Root")
                }
                noMatch {
                    Text("other")
                    Text(remainingPath)
                    val parameters = parameters
                    if (parameters != null) {
                        Text(parameters.raw)
                    }
                }
            }
        }
        assertEquals("Root", root.innerHTML)
        router.navigate("/foo")
        waitForRecompositionComplete()
        assertEquals("other/foo", root.innerHTML)

        router.navigate("/")
        waitForRecompositionComplete()
        assertEquals("Root", root.innerHTML)

        router.navigate("/foo", Parameters.from("V" to "b"))
        waitForRecompositionComplete()
        assertEquals("other/fooV=b", root.innerHTML)
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
    fun RouterCompositionLocalCurrentRouterTest() = runTest {
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
                            onDispose { }
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
    fun RouterCompositionLocalRouterCurrentTest() = runTest {
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
                            onDispose { }
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

    @ExperimentalUuidApi
    @Test
    fun relativeRoutingTest() = runTest {
        var router: Router = MockRouter()
        composition {
            router.route("/") {
                route("foo") {
                    int {
                        uuid {
                            Text("UUID $it")
                            router = Router.current
                        }
                        noMatch {
                            Text("Int $it")
                            router = Router.current
                        }
                    }
                    noMatch {
                        Text("Foo")
                        router = Router.current
                    }
                }
                noMatch {
                    Text("NoMatch")
                    router = Router.current
                }
            }
        }
        assertEquals("NoMatch", root.innerHTML)

        router.navigate("/foo")
        waitForRecompositionComplete()
        assertEquals("Foo", root.innerHTML)

        router.navigate("42")
        waitForRecompositionComplete()
        assertEquals("Int 42", root.innerHTML)

        router.navigate(Uuid.NIL.toString())
        waitForRecompositionComplete()
        assertEquals("UUID ${Uuid.NIL}", root.innerHTML)

        router.navigate("/")
        waitForRecompositionComplete()
        assertEquals("NoMatch", root.innerHTML)
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
