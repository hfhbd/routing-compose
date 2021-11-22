import androidx.compose.runtime.*
import app.softwork.routingcompose.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.Color.black
import org.jetbrains.compose.web.css.Color.white
import org.jetbrains.compose.web.dom.*

object DarkMode : StyleSheet() {
    init {
        "body" style {
            media("prefers-color-scheme: dark") {
                color(white)
                backgroundColor(black)
            }
        }
    }
}

fun main() {
    renderComposableInBody {
        Style(DarkMode)

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
    }
}

@Routing
@Composable
fun NavBuilder.Routing() {
    var foundAnswer by remember { mutableStateOf(false) }
    Text("Parameters: ${this.parameters?.map}")

    route("foo") {
        Text("Header for /foo")
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
                    Text("NavLink: Click to navigate to /foo/42 with a userID")
                }
            }
        }
        Text("Footer for /foo")
    }
    if(foundAnswer) {
        route("question") {
            Text("life, the universe and everything")
        }
    }
    int {
        if (it == 42) {
            SideEffect {
                foundAnswer = true
            }
            Text("Found the answer, new route /question is available")
            P {
                NavLink("/question") {
                    Text("NavLink: Click here to see the question")
                }
            }
        } else {
            Text("Wrong answer: $it")
        }
    }
    noMatch {
        P {
            Text("Hello Routing")
        }
        P {
            var answer by remember { mutableStateOf(0) }
            Input(InputType.Number) {
                onChange {
                    answer = it.value?.toInt() ?: 0
                }
                value(answer)
            }
            NavLink("/$answer") {
                Text("Submit answer")
            }
        }
        P {
            Text("Custom router usage")
        }
        P {
            Code {
                Text("val router = Router.current")
                Br()
                Text("router.navigate(to = /foo)")
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

@Composable
fun Clock() {
    val current by produceState(0) {
        while (true) {
            delay(1000)
            value += 1
        }
    }

    P {
        Text(current.toString())
    }
}
