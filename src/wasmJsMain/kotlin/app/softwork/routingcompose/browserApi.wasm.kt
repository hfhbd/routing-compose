package app.softwork.routingcompose

internal actual typealias Window = org.w3c.dom.Window

internal actual typealias History = org.w3c.dom.History

internal actual fun History.pushState(title: String, url: String?) {
    this.pushState(null, title, url)
}

internal actual typealias Location = org.w3c.dom.Location

internal actual val window: Window get() = kotlinx.browser.window
internal actual var Window.onpopstate: () -> Unit
    get() = { onpopstate() }
    set(value) {
        onpopstate = { value() }
    }
internal actual var Window.onhashchange: () -> Unit
    get() = { onhashchange() }
    set(value) {
        onhashchange = { value() }
    }
