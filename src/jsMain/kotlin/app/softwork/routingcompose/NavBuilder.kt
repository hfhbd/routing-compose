package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlinx.uuid.*

@NavBuilderDSL
public class NavBuilder internal constructor(private val node: RouteNode) {

    internal fun build(): Node = node

    @Composable
    public fun route(
        route: String,
        nestedRoute: @Composable NavBuilder.() -> Unit
    ) {
        val childNode = ConstantRouteNode(route)
        childNode.children += NavBuilder(childNode).apply { nestedRoute() }.build()
        node.children += childNode
    }

    @Composable
    public fun stringRoute(nestedRoute: @Composable NavBuilder.(Lazy<String>) -> Unit = {}) {
        val childNode = StringRouteNode()
        childNode.children += NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }.build()
        node.children += childNode
    }

    @Composable
    public fun string(content: @Composable (String) -> Unit) {
        val childNode = StringContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }

    @Composable
    public fun intRoute(nestedRoute: @Composable NavBuilder.(Lazy<Int>) -> Unit = {}) {
        val childNode = IntRouteNode()
        childNode.children += NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }.build()
        node.children += childNode
    }

    @Composable
    public fun int(content: @Composable (Int) -> Unit) {
        val childNode = IntContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }

    @Composable
    public fun uuidRoute(nestedRoute: @Composable NavBuilder.(Lazy<UUID>) -> Unit = {}) {
        val childNode = UUIDRouteNode()
        childNode.children += NavBuilder(childNode).apply {
            nestedRoute(lazy {
                childNode.variable
            })
        }.build()
        node.children += childNode
    }

    @Composable
    public fun uuid(content: @Composable (UUID) -> Unit) {
        val childNode = UUIDContentNode().apply {
            this.content = content
        }
        node.children += childNode
    }


    @Composable
    public fun noMatch(content: @Composable () -> Unit) {
        node.children += SimpleContentNode().apply { this.content = content }
    }
}
