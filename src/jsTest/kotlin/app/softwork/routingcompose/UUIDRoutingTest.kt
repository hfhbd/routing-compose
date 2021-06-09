package app.softwork.routingcompose

import kotlinx.uuid.*
import org.jetbrains.compose.web.dom.*
import kotlin.test.*

class UUIDRoutingTest {
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
                uuid {
                    Text("bar$it")
                }
                noMatch {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)

        router.navigate("/foo", "foo")

        val uuid = UUID()
        router.navigate("/$uuid", "bar$uuid")
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                route("/users") {
                    uuidRoute { userID ->
                        route("/todos") {
                            uuid { todoID ->
                                Text("Todo $todoID for user: ${userID.value}")
                            }
                            noMatch {
                                Text("All todos for user: ${userID.value}")
                            }
                        }
                        noMatch {
                            Text("UserInfo: ${userID.value}")
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
        val userID = UUID()
        router.navigate("/users/$userID", "UserInfo: $userID")
        router.navigate("/users/$userID/todos", "All todos for user: $userID")
        val todoID = UUID()
        router.navigate("/users/$userID/todos/$todoID", "Todo $todoID for user: $userID")
    }
}