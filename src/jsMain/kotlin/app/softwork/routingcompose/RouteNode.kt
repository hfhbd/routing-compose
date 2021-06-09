package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class RouteNode : Node() {
    public var children: List<Node> = emptyList()

    @Composable
    override fun execute(path: String) {
        val childPath = getChildPath(path)
        val matchedChild = children.firstOrNull { child ->
            child.matches(childPath)
        }

        val subRoute = getFullChildPath(fullPath = path, childPath = childPath)
        matchedChild?.execute(subRoute)
    }


    internal open fun getChildPath(fullPath: String): String = fullPath.removePrefix("/").takeWhile { it != '/' }

    private fun getFullChildPath(fullPath: String, childPath: String): String {
        if (fullPath == "/$childPath/") {
            return childPath
        }
        return fullPath.removePrefix("/$childPath")
    }
}