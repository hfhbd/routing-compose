import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import app.softwork.routingcompose.*

private const val Answer = 42

@Composable
fun RouteBuilder.Demo(
    root: Int,
    wasMagic42: Boolean,
    changeMagic42: (Boolean) -> Unit,
) {
    Column {
        val params = parameters?.map
        if (params != null) {
            Text("Parameters: $params")
        }
        if (wasMagic42) {
            route("/answer") {
                val router = Router.current
                Text("The Answer to the Ultimate Question of Life, the Universe, and Everything is 42.")
                Button(onClick = {
                    router.navigate("/$root")
                }) {
                    Text("Back")
                }
            }
        }
        int {
            Content(it, wasMagic42)
            if (it == Answer) {
                LaunchedEffect(it) {
                    changeMagic42(true)
                }
            }
        }
        string {
            Column {
                val router = Router.current
                Text("Hello $it")
                Button(onClick = {
                    router.navigate("/$root")
                }) {
                    Text("Back")
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
