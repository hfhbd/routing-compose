package app.softwork.routingcompose

public class ConstantRouteNode(public val node: String) : RouteNode() {
    override fun matches(subRoute: String): Boolean = subRoute == node
}