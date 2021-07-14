package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.browser.*

/**
 * This [Router] implementation uses `/#/path` to persistent the current route in [window.location.hash].
 *
 * Every request will always request `GET /`, so your server needs only to listen and serve this endpoint, or using a SaaS `/index.html`.
 */
public object HashRouter : AbstractRouter(initPath = window.location.hash.removePrefix("#")) {
    init {
        window.onhashchange = {
            val new: String = window.location.hash.removePrefix("#")
            update(newPath = new)
        }
    }

    override fun navigate(to: String) {
        require(to.startsWith("/"))
        window.location.hash = to
    }
}

