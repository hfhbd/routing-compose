package app.softwork.routingcompose

public class IntRouteNode : VariableRouteNode<Int>() {
    override fun fromPath(directRoute: String): Int? {
        println("\nfromPath $directRoute $this \nAAAA")
        return directRoute.toIntOrNull()
    }

    override fun toString(): String {
        return "IntRouteNode(children='$children')"
    }
}