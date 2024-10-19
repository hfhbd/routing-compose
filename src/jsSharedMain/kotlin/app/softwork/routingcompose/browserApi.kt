package app.softwork.routingcompose

internal expect abstract class Window {
    val history: History
    val location: Location
}

internal expect var Window.onpopstate: () -> Unit

internal expect var Window.onhashchange: () -> Unit

internal expect abstract class History
internal expect fun History.pushState(title: String, url: String?)
internal expect fun History.replaceState(title: String, url: String?)

internal expect abstract class Location {
    var pathname: String
    var search: String
    var hash: String
    fun assign(url: String)
    fun replace(url: String)
}

internal expect val window: Window
