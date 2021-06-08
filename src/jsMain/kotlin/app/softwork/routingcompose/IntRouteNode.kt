package app.softwork.routingcompose

public class IntRouteNode : VariableRouteNode<Int>() {
    override fun fromPath(directRoute: String): Int? = directRoute.toIntOrNull()
}