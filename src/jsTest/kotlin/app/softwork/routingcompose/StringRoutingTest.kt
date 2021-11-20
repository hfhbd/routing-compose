package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.testutils.*
import kotlin.test.*

@ComposeWebExperimentalTestsApi
class StringRoutingTest {
    @Test
    fun contentTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route("foo") {
                    noMatch {
                        Text("foo")
                    }
                }
                string {
                    Text("bar$it")
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
        assertEquals("barbar", root.innerHTML)
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route("users") {
                    string { userID ->
                        route("todos") {
                            string {
                                Text("Todo $it for user: $userID")
                            }
                            noMatch {
                                Text("All todos for user: $userID")
                            }
                        }
                        noMatch {
                            Text("UserInfo: $userID")
                        }
                    }
                    noMatch {
                        Text("No userID")
                    }
                }
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)

        router.navigate("/users")
        waitForRecompositionComplete()
        assertEquals("No userID", root.innerHTML)
        router.navigate("/users/john")
        waitForRecompositionComplete()
        assertEquals("UserInfo: john", root.innerHTML)
        router.navigate("/users/john/todos")
        waitForRecompositionComplete()
        assertEquals("All todos for user: john", root.innerHTML)
        router.navigate("/users/john/todos/first")
        waitForRecompositionComplete()
        assertEquals("Todo first for user: john", root.innerHTML)
    }

    @Test
    fun nested() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                string { userID ->
                    string { todoID ->
                        Text("Todo with $todoID from user $userID")
                    }
                    noMatch {
                        Text("User $userID")
                    }
                }
                noMatch {
                    Text("No userID given")
                }
            }
        }
        assertEquals("No userID given", root.innerHTML)
        router.navigate("/f")
        waitForRecompositionComplete()
        assertEquals("User f", root.innerHTML)
        router.navigate("/f/t")
        waitForRecompositionComplete()
        assertEquals("Todo with t from user f", root.innerHTML)
    }
}
