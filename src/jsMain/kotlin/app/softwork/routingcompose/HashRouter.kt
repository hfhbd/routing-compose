package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*

/**
 * This [Router] implementation uses `/#/path` to persistent the current route in [window.location.hash].
 *
 * Every request will always request `GET /`, so your server needs only to listen and serve this endpoint, or using a SaaS `/index.html`.
 */
public object HashRouter : Router() {
    @Composable
    override fun getPath(initPath: String): State<String> =
        produceState(initialValue = window.location.hash.removePrefix("#").ifBlank { initPath }) {
            window.onhashchange = {
                val update: String = window.location.hash.removePrefix("#").ifBlank { initPath }
                update.let {
                    value = it
                }
            }
        }

    override fun navigate(to: String) {
        window.location.hash = to
    }
}
