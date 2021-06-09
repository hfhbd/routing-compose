package app.softwork.routingcompose

import androidx.compose.runtime.*

public abstract class RouteNode : Node() {
    public var children: List<Node> = emptyList()

    @Composable
    override fun execute(path: String) {
        val childPath = getChildPath(path)
        println("\n\nexecute $path $this with childPath $childPath end\n\n")
        val matchedChild = children.firstOrNull { child ->
            child.matches(childPath)
        }

        val subRoute = getFullChildPath(fullPath = path, childPath = childPath)
        println("\n\nmatchedChild $matchedChild with subroute $subRoute from fullPath $path and childPath $childPath\n\n")
        matchedChild?.execute(subRoute)
    }


    internal open fun getChildPath(fullPath: String): String {
        return fullPath.removePrefix("/").takeWhile { it != '/' }
    }

    internal fun getFullChildPath(fullPath: String, childPath: String): String {
        if (fullPath == "/$childPath/") {
            return childPath
        }
        return fullPath.removePrefix("/$childPath")
    }
}