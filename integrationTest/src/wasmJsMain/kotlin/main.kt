
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.window.ComposeViewport
import app.softwork.routingcompose.*

@ExperimentalComposeUiApi
fun main() = ComposeViewport("WASM Test") {
    var wasMagic42 by remember { mutableStateOf(false) }
    HashRouter("/1") {
        Demo(1, wasMagic42, { wasMagic42 = it })
    }
}
