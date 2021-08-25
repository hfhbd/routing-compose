import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import app.softwork.routingcompose.*

fun main() = application {
    repeat(2) {
        var isOpen by remember { mutableStateOf(true) }
        if (isOpen) {
            Window(onCloseRequest = {
                isOpen = false
            }, title = "$it") {
                DesktopRouter("/$it") {
                    int {
                        Content(it)
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
