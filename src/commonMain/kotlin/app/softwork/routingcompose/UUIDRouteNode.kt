package app.softwork.routingcompose

import kotlinx.uuid.*

public class UUIDRouteNode : VariableRouteNode<UUID>() {
    override fun variable(fromRoute: String): UUID? = fromRoute.toUUIDOrNull()

    override fun toString(): String = "UUIDRouteNode(children='$children')"
}