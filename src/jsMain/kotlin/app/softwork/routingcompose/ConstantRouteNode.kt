package app.softwork.routingcompose

public class ConstantRouteNode(public val route: String) : RouteNode() {
    init {
        require(!route.startsWith("/")) { "$route must not start with a trailing slash." }
        require(!route.contains("/")) { "To use nested routes, use route() { route() { } } instead." }
    }

    override fun matches(subRoute: String): Boolean = subRoute == route

    override fun toString(): String = "ConstantRouteNode(route='$route',children='$children')"
}