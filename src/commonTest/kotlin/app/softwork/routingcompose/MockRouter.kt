package app.softwork.routingcompose

import androidx.compose.runtime.*

class MockRouter : Router {

    private val currentPath = mutableStateOf<String?>(null)

    @Composable
    override fun getPath(initPath: String) =
        derivedStateOf { currentPath.value ?: initPath }

    override fun navigate(to: String) {
        currentPath.value = to
    }
}

@Composable
operator fun MockRouter.invoke(initPath: String, routeBuilder: @Composable RouteBuilder.() -> Unit) =
    route(initPath, routeBuilder)
