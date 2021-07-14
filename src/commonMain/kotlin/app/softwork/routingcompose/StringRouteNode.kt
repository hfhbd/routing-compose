package app.softwork.routingcompose

public class StringRouteNode : VariableRouteNode<String>() {
    override fun variable(fromRoute: String): String? = fromRoute.takeIf { it.isNotEmpty() }

    override fun toString(): String = "StringRouteNode(children='$children')"
}