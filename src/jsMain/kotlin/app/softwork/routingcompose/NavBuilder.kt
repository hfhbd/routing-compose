package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

public class NavBuilder internal constructor(private val node: RouteNode) {

    @RouteBuilderDSL
    public fun route(
        route: String,
        nestedRoute: NavBuilder.() -> Unit
    ) {
        val childNode = ConstantRouteNode(route)
        NavBuilder(childNode).apply { nestedRoute() }
        node.children += childNode
    }

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

    @ContentBuilderDSL
    public fun string(content: @Composable (String) -> Unit) {
        val childNode = StringContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }


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

    @ContentBuilderDSL
    public fun int(content: @Composable (Int) -> Unit) {
        val childNode = IntContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }

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


    @ContentBuilderDSL
    public fun uuid(content: @Composable (UUID) -> Unit) {
        val childNode = UUIDContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }


    @ContentBuilderDSL
    public fun noMatch(content: @Composable () -> Unit) {
        node.children += SimpleContentNode().apply { this.content = content }
    }
}
