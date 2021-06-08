package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import kotlin.test.*

class IntegerRoutingTest {
    @Test
    fun contentTest() = runTest {
        val router = MockRouter()
        compose {
            router {
                route("/foo") {
                    Text("Foo")
                }
                int {
                    Text("bar$it")
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

        router.navigate("/5")
        waitForChanges()
        assertEquals("bar5", content)
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        compose {
            router {
                route("/users") {
                    intRoute { str ->
                        route("/todos") {
                            int {
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
        assertEquals("other", content)

        router.navigate("/users", "No userID")
        router.navigate("/users/5", "UserInfo: 5")
        router.navigate("/users/5/todos", "All todos for user: 5")
        router.navigate("/users/5/todos/42", "Todo 42 for user: 5")
    }
}