package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

public class NavBuilder internal constructor(private val node: RouteNode) {

    internal fun build(): Node = node

    @Composable
    @RouteBuilderDSL
    public fun route(
        route: String,
        nestedRoute: @Composable NavBuilder.() -> Unit
    ) {
        require(route.startsWith("/"))
        val childNode = NavBuilder(ConstantRouteNode(route)).apply { nestedRoute() }.build()
        node.children += childNode
    }

    @Composable
    @RouteBuilderDSL
    public fun stringRoute(nestedRoute: @Composable NavBuilder.(Lazy<String>) -> Unit = {}) {
        val childNode = StringRouteNode()
        NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }
        node.children += childNode
    }

    @Composable
    @ContentBuilderDSL
    public fun string(content: @Composable (String) -> Unit) {
        val childNode = StringContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }

    @Composable
    @RouteBuilderDSL
    public fun intRoute(nestedRoute: @Composable NavBuilder.(Lazy<Int>) -> Unit = {}) {
        val childNode = IntRouteNode()
        NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }
        node.children += childNode
    }

    @Composable
    @ContentBuilderDSL
    public fun int(content: @Composable (Int) -> Unit) {
        val childNode = IntContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }

    @Composable
    @RouteBuilderDSL
    public fun uuidRoute(nestedRoute: @Composable NavBuilder.(Lazy<UUID>) -> Unit = {}) {
        val childNode = UUIDRouteNode()
        NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }
        node.children += childNode
    }

    @Composable
    @ContentBuilderDSL
    public fun uuid(content: @Composable (UUID) -> Unit) {
        val childNode = UUIDContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }


    @Composable
    @ContentBuilderDSL
    public fun noMatch(id: String, content: @Composable () -> Unit) {
        node.children += SimpleContentNode(id).apply { this.content = content }
    }
}
