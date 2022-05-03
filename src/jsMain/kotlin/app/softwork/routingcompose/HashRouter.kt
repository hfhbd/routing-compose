package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*

/**
 * This [Router] implementation uses `/#/path` to persistent the current route in [window.location.hash].
 *
 * Every request will always request `GET /`, so your server needs only to listen and serve this endpoint, or using a SaaS `/index.html`.
 */
public object HashRouter : Router() {
    override val root: String = "#"

    @Composable
    override fun getPath(initPath: String): State<String> =
        produceState(initialValue = currentURL(fallback = initPath)) {
            window.onhashchange = {
                value = currentURL(fallback = initPath)
                Unit
            }
        }

    private fun currentURL(fallback: String) = window.location.hash
        .removePrefix("/").removePrefix("#")
        .ifBlank { fallback }

    override fun navigate(to: String) {
        window.location.hash = to
    }
}
