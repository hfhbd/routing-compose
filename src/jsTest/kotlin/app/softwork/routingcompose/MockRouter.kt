package app.softwork.routingcompose

import androidx.compose.runtime.*

class MockRouter(initPath: String = "") : Router {
    private val state = mutableStateOf(initPath)

    @Composable
    override fun getPath(): State<String> = state

    override fun navigate(to: String) {
        state.value = to
    }
}