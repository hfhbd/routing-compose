import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import app.softwork.routingcompose.*

fun main() = application {
    Demo(1)
    Demo(2)
}

private const val Answer = 42

@Composable
private fun Demo(it: Int) {
    var isOpen by remember { mutableStateOf(true) }
    var wasMagic42 by remember { mutableStateOf(false) }
    if (isOpen) {
        Window(onCloseRequest = {
            isOpen = false
        }, title = "$it") {
            DesktopRouter("/$it") {
                Column {
                    val params = parameters?.map
                    if (params != null) {
                        Text("Parameters: $params")
                    }
                    if (wasMagic42) {
                        route("/answer") {
                            val router = Router.current
                            Text("The Answer to the Ultimate Question of Life, the Universe, and Everything is 42.")
                            Button(onClick = { router.navigateBack() }) {
                                Text("Back")
                            }
                        }
                    }
                    int {
                        Content(it, wasMagic42)
                        if (it == Answer) {
                            LaunchedEffect(it) {
                                wasMagic42 = true
                            }
                        }
                    }
                    string {
                        Column {
                            val router = Router.current
                            Text("Hello $it")
                            Button(onClick = { router.navigateBack() }) {
                                Text("Back")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Content(int: Int, wasMagic42: Boolean) {
    var name by remember { mutableStateOf("") }
    val router = Router.current
    Column {
        Text("Hello World $int")
        TextField(value = name, {
            name = it
        })

        if (name.isNotBlank()) {
            Button({
                router.navigate("/$name")
            }) {
                Text("Update name")
            }
        }

        if (wasMagic42) {
            Button({
                router.navigate("/answer")
            }) {
                Text("Unlocked answer")
            }
        }
    }
}
