import androidx.compose.runtime.*
import app.softwork.routingcompose.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

fun main() {
    renderComposableInBody {
        Clock()

        P {
            Text("HashRouter implementation")
        }
        HashRouter("/") { Routing() }

        Hr()
        P {
            Text("BrowserRouter implementation")
        }
        BrowserRouter(initRoute = "/") { Routing() }

        P {
            Text("Custom router usage")
        }
        P {
            Code {
                Text("val router = Router.current")
                Text("router.navigate(to = /foo")
            }
        }

        val router = Router.current
        Input(type = InputType.Button) {
            onClick {
                router.navigate("/foo")
            }
            value("Click to navigate to /foo")
        }
    }
}

fun NavBuilder.Routing() {
    route("foo") {
        int {
            P { Text("Hello user $it") }
            P { Text("Use the back and forward functions of your browser to go navigate back.") }
        }
        noMatch {
            P {
                Text("Foo")
            }
            P {
                NavLink("/foo/42") {
                    Text("Click to navigate to /foo/42 with a userID")
                }
            }
        }
    }
    noMatch {
        P {
            Text("Hello Routing")
        }
        P {
            NavLink("/foo") {
                Text("Click to navigate to /foo")
            }
        }
    }
}

@Composable
fun Clock() {
    var current by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            current += 1
        }
    }

    P {
        Text(current.toString())
    }
}
