import androidx.compose.runtime.*
import app.softwork.routingcompose.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.*
import org.jetbrains.compose.web.dom.*

fun main() {
    renderComposableInBody {
        Clock()
        HashRouter(initRoute = "/") {
            route("foo") {
                int {
                    Text("Hello user $it")
                    Text("Use the back and forward functions of your browser to go navigate back.")
                }
                noMatch {
                    NavLink("/foo/42") {
                        Text("Navigate to foo with a userID")
                    }
                    P {
                        Text("Foo")
                    }
                }
            }
            noMatch {
                Text("Hello Routing")
                NavLink("/foo") {
                    Text("Navigate to foo")
                }
            }
        }
    }
}

@Composable
fun Clock() {
    val scope = rememberCoroutineScope()
    var current by remember { mutableStateOf(0) }

    SideEffect {
        scope.launch {
            while (true) {
                delay(1000)
                current += 1
            }
        }
    }

    P {
        Text(current.toString())
    }
}