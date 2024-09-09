
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.window.*
import app.softwork.routingcompose.*

@ExperimentalComposeUiApi
fun main() = CanvasBasedWindow("WASM Test") {
    var wasMagic42 by remember { mutableStateOf(false) }
    HashRouter("/1") {
        Demo(1, wasMagic42, { wasMagic42 = it })
    }
}
