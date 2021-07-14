import androidx.compose.desktop.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import app.softwork.routingcompose.*

fun main() {
    repeat(2) {
        Window(title = "$it") {
            DesktopRouter("/") {
                string {
                    Text("Hello $it")
                }
                noMatch {
                    Content()
                }
            }
        }
    }
}

@Composable
fun Content() {
    var name by remember { mutableStateOf("") }
    val router = Router.current
    Column {
        Text("Hello World")
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
