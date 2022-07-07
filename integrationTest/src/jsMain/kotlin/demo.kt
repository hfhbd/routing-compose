import androidx.compose.runtime.*
import app.softwork.routingcompose.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.Color.black
import org.jetbrains.compose.web.css.Color.white
import org.jetbrains.compose.web.dom.*
import kotlin.time.Duration.Companion.seconds

@Composable
fun Demo(router: Router, name: String) {
    Style(DarkMode)

    P {
        Text("$name implementation")
    }
    router.route("/") {
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

private const val Players = 5

@Composable
fun RouteBuilder.Users() {
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
        for (i in 0..Players) {
            P {
                Input(type = InputType.Button) {
                    onClick {
                        router.navigate("$i", Parameters.from("name" to "paul"))
                    }
                    value("Navigate to $i with the userName paul")
                }
            }
        }
        Br()
        Text("Footer for /users")
        Br()
        NavLink(to = "/") {
            Text("Go back to main page")
        }
        Br()
        NavLink(to = "./answer") {
            Text("Relative route to ./answer")
        }
    }
}

@Routing
@Composable
fun RouteBuilder.Routing() {
    var enableAnswer by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(5.seconds)
        enableAnswer = true
    }

    Clock()
    NavLink(to = "/", attrs = { selected ->
        if (selected) {
            classes("active")
        }
    }) { Text("Main") }
    Br()
    NavLink(to = "/users", attrs = { selected ->
        if (selected) {
            classes("active")
        }
    }) { Text("Users") }

    P {
        Text("Parameters: ${this@Routing.parameters?.map}")
    }
    route("users") {
        Users()
    }
    redirect("redirect", target = "users", hide = true)
    if (enableAnswer) {
        answer()
    }
    noMatch {
        Hello(enableAnswer) {
            enableAnswer = it
        }
    }
}

@Composable
private fun Hello(enableAnswer: Boolean, updateEnableAnswer: (Boolean) -> Unit) {
    P {
        Text("Hello Routing")
    }
    if (enableAnswer) {
        NavLink("answer") {
            Text("Click to navigate to /answer")
        }
    } else {
        Button({
            onClick {
                updateEnableAnswer(true)
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
                router.navigate("users")
            }
            value("Navigate to /users")
        }
    }
    Hr()
    RedirectButton()
}

@Routing
@Composable
fun RouteBuilder.answer() {
    route("answer") {
        int {
            Br()
            Text("The Answer to the Ultimate Question of Life, the Universe, and Everything is $it.")
        }
        noMatch {
            redirect("42", hide = true)
        }
    }
}

@Composable
fun RedirectButton() {
    Text("You could also hide navigations and redirect path to other routes.")
    val router = Router.current
    Input(type = InputType.Button) {
        onClick {
            router.navigate("redirect", hide = true)
        }
        value("Navigate to /users. Check the url.")
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
