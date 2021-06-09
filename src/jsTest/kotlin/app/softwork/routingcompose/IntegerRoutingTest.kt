package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import kotlin.test.*

class IntegerRoutingTest {
    @Test
    fun contentTest() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                route("/foo") {
                    noMatch("foo") {
                        Text("foo")
                    }
                }
                int {
                    Text("bar$it")
                }
                noMatch("other") {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)

        router.navigate("/foo", "foo")

        router.navigate("/5", "bar5")
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                route("/users") {
                    intRoute { str ->
                        route("/todos") {
                            int {
                                Text("Todo $it for user: ${str.value}")
                            }
                            noMatch("allTodos") {
                                Text("All todos for user: ${str.value}")
                            }
                        }
                        noMatch("userInfo") {
                            Text("UserInfo: ${str.value}")
                        }
                    }
                    noMatch("noUserID") {
                        Text("No userID")
                    }
                }
                noMatch("other") {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)

        router.navigate("/users", "No userID")
        router.navigate("/users/5", "UserInfo: 5")
        router.navigate("/users/5/todos", "All todos for user: 5")
        router.navigate("/users/5/todos/42", "Todo 42 for user: 5")
    }
}