import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import app.softwork.routingcompose.*

fun main() = application {
    Demo(1)
    Demo(2)
}

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
                    if (wasMagic42) {
                        int {
                            Text("Answer of the universe")
                        }
                    } else {
                        int {
                            Content(it)
                            if (it == 42) {
                                wasMagic42 = true
                            }
                        }
                    }
                    string {
                        Column {
                            Text("Hello $it")
                            val router = Router.current
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
fun Content(int: Int) {
    var name by remember { mutableStateOf("") }
    val router = Router.current
    Column {
        Text("Hello World $int")
        TextField(value = name, {
            name = it
        })

        Button({
            router.navigate("/$name")
        }) {
            Text("Update name")
        }
    }
}
