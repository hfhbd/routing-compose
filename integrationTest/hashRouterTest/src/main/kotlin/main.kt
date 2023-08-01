import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposableInBody
import kotlin.random.Random

private class T(var s: (@Composable () -> Unit)? = null)

fun main() {
    renderComposableInBody {
        val t = remember { T() }
        val i = Random.nextInt(3)
        if (i == 0) {
            t.s = {
                trueText()
            }
        }
        if (i == 1) {
            t.s = {
                Text("false")
            }
        }
        if (t.s == null) {
            t.s = {
                Text("Fallback")
            }
        }
        t.s?.invoke()
    }
}

@Composable
fun trueText() {
    println("called")
    Text("true")
}
