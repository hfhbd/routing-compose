import app.softwork.routingcompose.*
import org.jetbrains.compose.web.*

fun main() {
    renderComposableInBody {
        BrowserRouter("/") {
            Demo("BrowserRouter")
        }
    }
}
