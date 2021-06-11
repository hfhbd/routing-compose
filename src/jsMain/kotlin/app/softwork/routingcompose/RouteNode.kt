package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class RouteNode : Node() {
    public var children: List<Node> = emptyList()

    @Composable
    public open fun execute(path: String) {
        val childPath = getChildPath(path)
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


    internal open fun getChildPath(fullPath: String): String = fullPath.removePrefix("/").takeWhile { it != '/' }
}