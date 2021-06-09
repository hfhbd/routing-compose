package app.softwork.routingcompose

public class RootNode : RouteNode() {
    override fun matches(subRoute: String): Boolean {
        println("matches $this $subRoute\nAAAA")
        return subRoute.isEmpty()
    }

    override fun toString(): String {
        return "RootNode(children='$children')"
    }
}