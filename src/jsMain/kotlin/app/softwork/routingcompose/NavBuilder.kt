package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

/**
 * Use the DSL functions to build the expected route handled by a [Router].
 * If two routes matches the same path, the first declared route is choosen.
 *
 * Unfortunately, every `@Composable` block has to be inside a [ContentNode].
 */
public class NavBuilder internal constructor(private val node: RouteNode) {

    /**
     * Executes its children when the requested subroute matches this constant [route].
     *
     * To match `foo/bar`, create a [route] inside the first [route].
     */
    @RouteBuilderDSL
    public fun route(
        route: String,
        nestedRoute: NavBuilder.() -> Unit
    ) {
        val childNode = ConstantRouteNode(route)
        NavBuilder(childNode).apply { nestedRoute() }
        node.children += childNode
    }

    /**
     * Executes its children when the requested subroute is a [String].
     *
     * To get the route parameter, call the [Lazy] parameter inside a [ContentNode].
     */
    @RouteBuilderDSL
    public fun stringRoute(nestedRoute: NavBuilder.(Lazy<String>) -> Unit = {}) {
        val childNode = StringRouteNode()
        NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }
        node.children += childNode
    }

    /**
     * Displays this `@Composable` block when the last subroute is a [String].
     */
    @ContentBuilderDSL
    public fun string(content: @Composable (String) -> Unit) {
        val childNode = StringContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }


    /**
     * Executes its children when the requested subroute is a [Int].
     *
     * To get the route parameter, call the [Lazy] parameter inside a [ContentNode].
     */
    @RouteBuilderDSL
    public fun intRoute(nestedRoute: NavBuilder.(Lazy<Int>) -> Unit = {}) {
        val childNode = IntRouteNode()
        NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }
        node.children += childNode
    }

    /**
     * Displays this `@Composable` block when the last subroute is a [Int].
     */
    @ContentBuilderDSL
    public fun int(content: @Composable (Int) -> Unit) {
        val childNode = IntContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }


    /**
     * Executes its children when the requested subroute is a [UUID].
     *
     * To get the route parameter, call the [Lazy] parameter inside a [ContentNode].
     */
    @RouteBuilderDSL
    public fun uuidRoute(nestedRoute: NavBuilder.(Lazy<UUID>) -> Unit = {}) {
        val childNode = UUIDRouteNode()
        NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }
        node.children += childNode
    }

    /**
     * Displays this `@Composable` block when the last subroute is a [UUID].
     */
    @ContentBuilderDSL
    public fun uuid(content: @Composable (UUID) -> Unit) {
        val childNode = UUIDContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }

    /**
     * Always displays this `@Composable` when called by [RouteNode.execute].
     */
    @ContentBuilderDSL
    public fun noMatch(content: @Composable () -> Unit) {
        node.children += SimpleContentNode().apply { this.content = content }
    }
}
