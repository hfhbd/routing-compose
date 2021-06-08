package app.softwork.routingcompose

import kotlinx.uuid.*
import org.jetbrains.compose.web.dom.*
import kotlin.test.*

class UUIDRoutingTest {
    @Test
    fun contentTest() = runTest {
        val router = MockRouter()
        compose {
            router {
                route("/foo") {
                    Text("Foo")
                }
                uuid {
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

        val uuid = UUID()
        router.navigate("/$uuid")
        waitForChanges()
        assertEquals("bar$uuid", content)
    }

    @Test
    fun routeTest() = runTest {
        val router = MockRouter()
        compose {
            router {
                route("/users") {
                    uuidRoute { uuid ->
                        route("/todos") {
                            uuid {
                                Text("Todo $it for user: ${uuid.value}")
                            }
                            noMatch {
                                Text("All todos for user: ${uuid.value}")
                            }
                        }
                        noMatch {
                            Text("UserInfo: ${uuid.value}")
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
        val userID = UUID()
        router.navigate("/users/$userID", "UserInfo: $userID")
        router.navigate("/users/$userID/todos", "All todos for user: $userID")
        val todoID = UUID()
        router.navigate("/users/$userID/todos/$todoID", "Todo $todoID for user: $userID")
    }
}