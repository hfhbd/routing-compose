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
                    noMatch("foo") {
                        Text("foo")
                    }
                }
                string {
                    Text("bar$it")
                }
                noMatch("other") {
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
                            noMatch("todos") {
                                Text("All todos for user: ${str.value}")
                            }
                        }
                        noMatch("userInfo") {
                            Text("UserInfo: ${str.value}")
                        }
                    }
                    noMatch("noUser") {
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
        router.navigate("/users/john", "UserInfo: john")
        router.navigate("/users/john/todos", "All todos for user: john")
        router.navigate("/users/john/todos/first", "Todo first for user: john")
    }
}