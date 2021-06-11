package app.softwork.routingcompose

public class ConstantRouteNode(node: String) : RouteNode() {
    init {
        require(node.startsWith("/")) { "$node must start with a trailing slash" }
    }

    public val node: String = node.removePrefix("/")

    override fun matches(subRoute: String): Boolean = subRoute == node

    override fun toString(): String = "ConstantRouteNode(node='$node',children='$children')"
}