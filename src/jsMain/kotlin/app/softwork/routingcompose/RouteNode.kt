package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class RouteNode : Node() {
    public var children: List<Node> = emptyList()

    @Composable
    override fun execute(subRoute: String) {
        val directRoute = subRoute.takeWhile { it != '/' }
        val matchedChild = children.firstOrNull { child ->
            child.matches(directRoute)
        }
        matchedChild?.execute(subRoute.dropWhile { it != '/' })
    }
}