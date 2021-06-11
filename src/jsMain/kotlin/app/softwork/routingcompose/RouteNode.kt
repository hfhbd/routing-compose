package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class RouteNode : Node() {
    public var children: List<Node> = emptyList()

    @Composable
    public fun execute(path: String) {
        val childPath = path.removePrefix("/").takeWhile { it != '/' }
        val matchedChild = children.firstOrNull { child ->
            child.matches(childPath)
        }

        when (matchedChild) {
            is ContentNode -> {
                matchedChild.display(childPath)
            }
            is RouteNode -> {
                val subRoute = path.removePrefix("/$childPath")
                matchedChild.execute(subRoute)
            }
        }
    }
}