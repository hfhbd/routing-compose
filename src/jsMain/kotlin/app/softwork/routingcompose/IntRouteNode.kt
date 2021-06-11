package app.softwork.routingcompose

public class IntRouteNode : VariableRouteNode<Int>() {
    override fun variable(fromRoute: String): Int? = fromRoute.toIntOrNull()

    override fun toString(): String = "IntRouteNode(children='$children')"
}