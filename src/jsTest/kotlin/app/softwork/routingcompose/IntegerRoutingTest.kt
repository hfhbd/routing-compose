package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.testutils.*
import kotlin.test.*

@ComposeWebExperimentalTestsApi
class IntegerRoutingTest {
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
                int {
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

        router.navigate("/5")
        waitForRecompositionComplete()
        assertEquals("bar5", root.innerHTML)
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route("users") {
                    int { userID ->
                        route("todos") {
                            int {
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
        router.navigate("/users/5")
        waitForRecompositionComplete()
        assertEquals("UserInfo: 5", root.innerHTML)
        router.navigate("/users/5/todos")
        waitForRecompositionComplete()
        assertEquals("All todos for user: 5", root.innerHTML)
        router.navigate("/users/5/todos/42")
        waitForRecompositionComplete()
        assertEquals("Todo 42 for user: 5", root.innerHTML)
    }

    @Test
    fun nested() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                int { userID ->
                    int { todoID ->
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
        router.navigate("/42")
        waitForRecompositionComplete()
        assertEquals(expected = "User 42", actual = root.innerHTML)
        router.navigate("/42/42")
        waitForRecompositionComplete()
        assertEquals(expected = "Todo with 42 from user 42", actual = root.innerHTML)
    }

    @Test
    fun invalide() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                int {
                    Text("int $it")
                }
                noMatch {
                    Text("noMatch")
                }
            }
        }
        assertEquals("noMatch", root.innerHTML)

        router.navigate("/foo")
        waitForRecompositionComplete()
        assertEquals("noMatch", root.innerHTML)
    }
}
