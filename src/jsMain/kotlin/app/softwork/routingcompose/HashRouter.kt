package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*

/**
 * This [Router] implementation uses `/#/path` to persistent the current route in [window.location.hash].
 *
 * Every request will always request `GET /`, so your server needs only to listen and serve this endpoint,
 * or using a SaaS `/index.html`.
 */
@Composable
public fun HashRouter(
    initPath: String,
    routeBuilder: @Composable RouteBuilder.() -> Unit
) {
    HashRouter().route(initPath, routeBuilder)
}

public class HashRouter : Router {

    private val currentPath: MutableState<String> = mutableStateOf(window.location.hash.currentURL(null))

    @Composable
    override fun getPath(initPath: String): State<String> {
        LaunchedEffect(Unit) {
            window.onhashchange = {
                currentPath.value = window.location.hash.currentURL(null)
                Unit
            }
        }
        return derivedStateOf { currentPath.value.ifBlank { initPath } }
    }

    private fun String.currentURL(fallback: String?) = removePrefix("#")
        .removePrefix("/")
        .ifBlank { fallback ?: "" }

    override fun navigate(to: String, hide: Boolean) {
        if (hide) {
            currentPath.value = to.currentURL(null)
        } else {
            window.location.hash = to
        }
    }
}
