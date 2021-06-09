package app.softwork.routingcompose

public class StringRouteNode : VariableRouteNode<String>() {
    override fun fromPath(directRoute: String): String? {

        return directRoute.takeIf {
            println("takeIF $directRoute $it isNotEmpty ${it.isNotEmpty()} taken ${it.takeIf { it.isNotEmpty() }} $this \nAAAA")
            it.isNotEmpty()
        }
    }

    override fun toString(): String {
        return "StringRouteNode(children='$children')"
    }
}