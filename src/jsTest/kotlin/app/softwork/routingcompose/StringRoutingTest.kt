package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import kotlin.test.*

class StringRoutingTest {
    @Test
    fun contentTest() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                route("/foo") {
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

        router.navigate("/foo", "foo")
        router.navigate("/bar", "barbar")
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                route("/users") {
                    stringRoute { str ->
                        route("/todos") {
                            string {
                                Text("Todo $it for user: ${str.value}")
                            }
                            noMatch {
                                Text("All todos for user: ${str.value}")
                            }
                        }
                        noMatch {
                            Text("UserInfo: ${str.value}")
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

        router.navigate("/users", "No userID")
        router.navigate("/users/john", "UserInfo: john")
        router.navigate("/users/john/todos", "All todos for user: john")
        router.navigate("/users/john/todos/first", "Todo first for user: john")
    }

    @Test
    fun nested() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                stringRoute { userID ->
                    string { todoID ->
                        Text("Todo with $todoID from user ${userID.value}")
                    }
                    noMatch {
                        Text("User ${userID.value}")
                    }
                }
                noMatch {
                    Text("No userID given")
                }
            }
        }
        assertEquals("No userID given", root.innerHTML)
        router.navigate("/f", expected = "User f")
        router.navigate("/f/t", expected = "Todo with t from user f")
    }
}