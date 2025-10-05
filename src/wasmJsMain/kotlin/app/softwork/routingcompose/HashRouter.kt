package app.softwork.routingcompose

import androidx.compose.runtime.*

/**
 * This [Router] implementation uses `/#/path` to persistent the current route in [window.location.hash].
 *
 * Every request will always request `GET /`, so your server needs only to listen and serve this endpoint,
 * or using a SaaS `/index.html`.
 */
@Deprecated("Use org.jetbrains.androidx.navigation:navigation-compose for Desktop and WASM instead")
@Composable
public actual fun HashRouter(
    initPath: String,
    routeBuilder: @Composable RouteBuilder.() -> Unit
) {
    HashRouter().route(initPath, routeBuilder)
}
