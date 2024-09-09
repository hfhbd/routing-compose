import androidx.compose.runtime.*
import androidx.compose.ui.window.*
import app.softwork.routingcompose.*

fun main() = application {
    DemoWindow(1)
    DemoWindow(2)
}

@Composable
fun DemoWindow(it: Int) {
    var isOpen by remember { mutableStateOf(true) }
    var wasMagic42 by remember { mutableStateOf(false) }
    if (isOpen) {
        Window(onCloseRequest = {
            isOpen = false
        }, title = "$it") {
            DesktopRouter("/$it") {
                Demo(it, wasMagic42, { wasMagic42 = it })
            }
        }
    }
}
