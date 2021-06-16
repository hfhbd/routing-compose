package app.softwork.routingcompose

import androidx.compose.runtime.*

/**
 * A [Node] handling the requested subroute by calling [Node.matches] to get the first child from [children].
 */
public abstract class RouteNode : Node() {
    public var children: List<Node> = emptyList()

    /**
     * Get the first matching child from the requested subroute by calling [Node.matches].
     * If the child is a [ContentNode] its `@Composable` block will be displayed, otherwise the subroute will be executed
     * again to finally find a [ContentNode].
     *
     * If no [ContentNode] is found, aka the requested route has no matching handler, [RouteNode] throws an
     * [IllegalStateException]. To prevent this exception, always add a [NavBuilder.noMatch] for each route.
     */
    @Composable
    public fun execute(path: String) {
        val childPath = path.removePrefix("/").takeWhile { it != '/' }
        val matchedChild = children.firstOrNull { child ->
            child.matches(childPath)
        } ?: error("No node matching $childPath of $path found. Please add a noMatch route.")

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