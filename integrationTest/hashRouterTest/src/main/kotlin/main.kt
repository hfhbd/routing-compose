import app.softwork.routingcompose.*
import org.jetbrains.compose.web.*

fun main() {
    renderComposableInBody {
        HashRouter("/") {
            Demo("HashRouter")
        }
    }
}
