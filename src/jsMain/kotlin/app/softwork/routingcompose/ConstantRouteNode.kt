package app.softwork.routingcompose

public class ConstantRouteNode(public val node: String) : RouteNode() {
    init {
        require(node.startsWith("/"))
    }

    override fun matches(subRoute: String): Boolean = subRoute == node.drop(1)

    override fun getChildPath(fullPath: String): String {
        return if (fullPath.startsWith("/")) {
            super.getChildPath(fullPath)
        } else {
            ""
        }
    }

    override fun toString(): String = "ConstantRouteNode(node='$node',children='$children')"
}