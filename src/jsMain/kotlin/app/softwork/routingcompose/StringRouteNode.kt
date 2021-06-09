package app.softwork.routingcompose

public class StringRouteNode : VariableRouteNode<String>() {
    override fun fromPath(directRoute: String): String? = directRoute.takeIf { it.isNotEmpty() }

    override fun toString(): String = "StringRouteNode(children='$children')"
}