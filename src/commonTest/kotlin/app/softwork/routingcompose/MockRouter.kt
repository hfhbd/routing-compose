package app.softwork.routingcompose

import androidx.compose.runtime.*

class MockRouter : Router {
    override val currentPath: Path
        get() = Path.from(currentState.value!!)

    private val currentState = mutableStateOf<String?>(null)

    @Composable
    override fun getPath(initPath: String) =
        derivedStateOf { currentState.value ?: initPath }

    override fun navigate(to: String, hide: Boolean) {
        currentState.value = to
    }
}
