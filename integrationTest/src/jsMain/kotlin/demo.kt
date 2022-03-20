import androidx.compose.runtime.*
import app.softwork.routingcompose.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.Color.black
import org.jetbrains.compose.web.css.Color.white
import org.jetbrains.compose.web.dom.*
import kotlin.time.*
import kotlin.time.Duration.Companion.seconds

@Composable
fun Demo(router: Router) {
    Style(DarkMode)

    P {
        Text("HashRouter implementation")
    }
    router("/") {
        Routing()
    }
}

object DarkMode : StyleSheet() {
    init {
        media("prefers-color-scheme", StylePropertyValue("dark")) {
            "body" style {
                color(white)
                backgroundColor(black)
            }
        }
        media("prefers-color-scheme", StylePropertyValue("light")) {
            "body" style {
                color(black)
                backgroundColor(white)
            }
        }
    }
}

@Routing
@Composable
fun NavBuilder.Routing() {
    var enableAnswer by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        @OptIn(ExperimentalTime::class)
        delay(5.seconds)
        enableAnswer = true
    }

    Clock()

    P {
        Text("Parameters: ${this@Routing.parameters?.map}")
    }
    route("users") {
        P {
            Text("Header for /users")
        }
        int { userID ->
            P { Text("Hello user $userID") }
            P { Text("Use the back and forward functions of your browser to go navigate back.") }
        }

        noMatch {
            P {
                Text("Users")
            }
            val router = Router.current
            for (i in 0..5) {
                P {
                    Input(type = InputType.Button) {
                        onClick {
                            router.navigate("/users/$i?name=paul")
                        }
                        value("Navigate to /users/$i with the user name paul")
                    }
                }
            }
            Br()
            Text("Footer for /users")
            Br()
            A(href = "#") {
                Text("Go back to main page")
            }
        }
    }
    if (enableAnswer) {
        route("answer") {
            Br()
            Text("The Answer to the Ultimate Question of Life, the Universe, and Everything is 42.")
        }
    }
    noMatch {
        P {
            Text("Hello Routing")
        }
        if (enableAnswer) {
            NavLink("/answer") {
                Text("Click to navigate to /answer")
            }
        } else {
            Button({
                onClick {
                    enableAnswer = true
                }
            }) {
                Text("Enable answer")
            }
        }
        Hr()
        P {
            Text("Custom router usage")
        }

        Code {
            Text("val router = Router.current")
            Br()
            Text("router.navigate(to = /users)")
        }

        val router = Router.current

        P {
            Input(type = InputType.Button) {
                onClick {
                    router.navigate("/users")
                }
                value("Navigate to /users")
            }
        }
    }
}

@Composable
fun Clock() {
    val current by remember {
        flow {
            var i = 0
            while (true) {
                emit(i)
                delay(timeMillis = 1000)
                i += 1
            }
        }
    }.collectAsState(0)

    P {
        Text(current.toString())
    }
}
