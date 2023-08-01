package app.softwork.routingcompose

import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.testutils.ComposeWebExperimentalTestsApi
import org.jetbrains.compose.web.testutils.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalSerializationApi
@ComposeWebExperimentalTestsApi
class TypedRoutingTest {
    @Test
    fun simple() = runTest {
        val router = MockRouter()
        composition {
            router("/") {
                route<Users>({
                    Text("no user match")
                }) {
                    Text("user ${it.userID}")
                }
                int {
                    Text("bar$it")
                }
                noMatch {
                    Text("noMatch")
                }
            }
        }
        waitForRecompositionComplete()
        assertEquals("noMatch", root.innerHTML)

        router.navigate("/users/42")
        waitForRecompositionComplete()
        assertEquals("user 42", root.innerHTML)

        router.navigate("/users")
        waitForRecompositionComplete()
        waitForRecompositionComplete()
        assertEquals("no user match", root.innerHTML)

        router.navigate("/5")
        waitForRecompositionComplete()
        assertEquals("bar5", root.innerHTML)
    }

    @Route("/foo/:bar")
    data class Foo(
        val bar: Inner
    ) {
        @Route("/inner/:bb")
        data class Inner(
            val bb: String
        )
    }

    @Test
    fun customSerializer() = runTest {
    }
}
