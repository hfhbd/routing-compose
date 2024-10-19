package app.softwork.routingcompose

import androidx.compose.runtime.*

class MockRouter : Router {
    override val currentPath: Path
        get() = Path.from(currentState.value!!)

    private val currentState = mutableStateOf<String?>(null)

    @Composable
    override fun getPath(initPath: String) =
        derivedStateOf { currentState.value ?: initPath }

    override fun navigate(to: String, hide: Boolean, replace: Boolean) {
        currentState.value = to
    }
}

@Composable
operator fun MockRouter.invoke(initPath: String, routeBuilder: @Composable RouteBuilder.() -> Unit) =
    route(initPath, routeBuilder)
