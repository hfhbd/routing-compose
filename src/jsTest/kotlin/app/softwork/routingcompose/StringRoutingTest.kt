package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import kotlin.test.*

class StringRoutingTest {
    @Test
    fun contentTest() = runTest {
        val router = MockRouter()
        compose {
            router {
                route("/foo") {
                    Text("Foo")
                }
                string {
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

        router.navigate("/bar")
        waitForChanges()
        assertEquals("barbar", content)
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        compose {
            router {
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
        assertEquals("other", content)

        router.navigate("/users", "No userID")
        router.navigate("/users/john", "UserInfo: john")
        router.navigate("/users/john/todos", "All todos for user: john")
        router.navigate("/users/john/todos/first", "Todo first for user: john")
    }
}