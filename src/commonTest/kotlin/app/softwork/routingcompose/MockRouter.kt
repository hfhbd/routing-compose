package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

class MockRouter(
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : Router {
    override val currentPath: Path
        get() = Path.from(currentState.value!!)

    private val currentState = mutableStateOf<String?>(null)

    @Composable
    override fun getPath(initPath: String) =
        derivedStateOf { currentState.value ?: initPath }

    override fun navigate(to: String, hide: Boolean) {
        currentState.value = to
    }

    override fun toString(): String = "/"
}

@Composable
operator fun MockRouter.invoke(initPath: String, routeBuilder: @Composable RouteBuilder.() -> Unit) =
    route(initPath, routeBuilder)
