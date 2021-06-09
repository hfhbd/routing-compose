package app.softwork.routingcompose

import kotlinx.uuid.*

public class UUIDRouteNode : VariableRouteNode<UUID>() {
    override fun fromPath(directRoute: String): UUID? {
        println("fromPath $directRoute $this\nAAAA")
        return directRoute.toUUIDOrNull()
    }

    override fun toString(): String {
        return "UUIDRouteNode(children='$children')"
    }
}