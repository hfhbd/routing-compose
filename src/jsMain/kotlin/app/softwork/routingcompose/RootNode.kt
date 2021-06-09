package app.softwork.routingcompose

public class RootNode : RouteNode() {
    override fun matches(subRoute: String): Boolean = subRoute.isEmpty()

    override fun toString(): String = "RootNode(children='$children')"
}