package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import kotlin.jvm.*

public interface Router {
    public val serializersModule: SerializersModule

    /**
     * The current path
     */
    public val currentPath: Path

    public fun navigate(to: String, hide: Boolean = false)

    @Composable
    public fun getPath(initPath: String): State<String>

    public companion object {
        /**
         * Provide the router implementation through a CompositionLocal so deeper level
         * Composables in the composition can have access to the current router.
         *
         * This is particularly useful for [NavLink], so we can have a single Composable
         * agnostic of the top level router implementation.
         *
         * To use this composition, you need to invoke any [Router] implementation first.
         */
        public val current: Router
            @Composable
            get() = RouterCompositionLocal.current
    }
}

internal val RouterCompositionLocal: ProvidableCompositionLocal<Router> =
    compositionLocalOf { error("Router not defined, cannot provide through RouterCompositionLocal.") }

@Composable
public fun Router.route(
    initRoute: String,
    routing: @Composable RouteBuilder.() -> Unit
) {
    CompositionLocalProvider(RouterCompositionLocal provides this) {
        val rawPath by getPath(initRoute)
        val path = Path.from(rawPath)
        val root = remember(path) { RouteBuilder(path.path, path) }
        println("$root\n")
        root.routing()
        root.show()
    }
}

public fun Router.navigate(to: String, parameters: Parameters, hide: Boolean = false) {
    navigate("$to?$parameters", hide = hide)
}

@JvmName("navigateParameterList")
public fun Router.navigate(to: String, parameters: Map<String, List<String>>, hide: Boolean = false) {
    navigate(to, Parameters.from(parameters), hide = hide)
}

public fun Router.navigate(to: String, parameters: Map<String, String>, hide: Boolean = false) {
    navigate(to, Parameters.from(parameters), hide = hide)
}

public inline fun <reified T : Any> Router.navigate(
    to: T,
    absolute: Boolean = true,
    hide: Boolean = false
) {
    navigate(to = to, serializer = serializer<T>(), absolute = absolute, hide = hide)
}

@OptIn(ExperimentalSerializationApi::class)
public fun <T : Any> Router.navigate(
    to: T,
    serializer: SerializationStrategy<T>,
    absolute: Boolean = true,
    hide: Boolean = false
) {
    navigate(
        to = buildString {
            if (absolute) {
                append("/")
            }
            serializer.serialize(
                RoutingEncoder(serializersModule, this),
                to
            )
        },
        hide = hide
    )
}

@Composable
public fun <T> RouteBuilder.rememberParameter(key: String): MutableState<List<T>?> {
    val router = Router.current
    return remember(key) {
        @Suppress("UNCHECKED_CAST")
        val state = mutableStateOf(parameters?.map?.get(key)?.map { it as T })
        UpdatedParameterState(state) {
            val old = parameters?.map?.toMutableMap() ?: mutableMapOf()
            if (it != null) {
                old[key] = it.map { it.toString() }
            } else {
                old.remove(key)
            }
            router.navigate(to = router.currentPath.path, parameters = old)
        }
    }
}

private class UpdatedParameterState<T>(
    private val state: MutableState<T>,
    private val update: (T) -> Unit
) : MutableState<T> by state {
    override var value: T
        get() = state.value
        set(value) {
            update(value)
            state.value = value
        }
}
