package app.softwork.routingcompose

import kotlinx.uuid.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.testutils.*
import kotlin.test.*

@ComposeWebExperimentalTestsApi
class UUIDRoutingTest {
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
                uuid {
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

        val uuid = UUID()
        router.navigate("/$uuid")
        waitForRecompositionComplete()
        assertEquals("bar$uuid", root.innerHTML)
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route("users") {
                    uuid { userID ->
                        route("todos") {
                            uuid { todoID ->
                                Text("Todo $todoID for user: $userID")
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
        val userID = UUID()
        router.navigate("/users/$userID")
        waitForRecompositionComplete()
        assertEquals("UserInfo: $userID", root.innerHTML)
        router.navigate("/users/$userID/todos")
        waitForRecompositionComplete()
        assertEquals("All todos for user: $userID", root.innerHTML)
        val todoID = UUID()
        router.navigate("/users/$userID/todos/$todoID")
        waitForRecompositionComplete()
        assertEquals("Todo $todoID for user: $userID", root.innerHTML)
    }

    @Test
    fun nested() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                uuid { userID ->
                    uuid { todoID ->
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
        val userID = UUID()
        router.navigate("/$userID")
        waitForRecompositionComplete()
        assertEquals("User $userID", root.innerHTML)
        val todoID = UUID()
        router.navigate("/$userID/$todoID")
        waitForRecompositionComplete()
        assertEquals("Todo with $todoID from user $userID", root.innerHTML)
    }
}
