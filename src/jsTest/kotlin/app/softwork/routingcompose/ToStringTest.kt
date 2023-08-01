package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.testutils.ComposeWebExperimentalTestsApi
import org.jetbrains.compose.web.testutils.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ComposeWebExperimentalTestsApi
class ToStringTest {
    @Test
    fun testToString() = runTest {
        val mockRouter = MockRouter()
        composition {
            mockRouter("/") {
                route("foo") {
                    val router = Router.current
                    Text(router.toString())
                }
                int {
                    val router = Router.current
                    Text(router.toString())

                    string {
                        val stringRouter = Router.current
                        Text(stringRouter.toString())
                    }
                }
                noMatch {
                    val router = Router.current
                    Text(router.toString())
                }
            }
        }
        assertEquals("/", root.innerHTML)

        mockRouter.navigate("/foo")
        waitForChanges()
        assertEquals("/foo", root.innerHTML)

        mockRouter.navigate("/42")
        waitForChanges()
        assertEquals("/:int", root.innerHTML)

        mockRouter.navigate("/42/foo")
        waitForChanges()
        assertEquals("/:int/:string", root.innerHTML)
    }
}
