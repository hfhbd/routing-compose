package app.softwork.routingcompose

public class RootNode : RouteNode() {
    override fun matches(subRoute: String): Boolean = true
}