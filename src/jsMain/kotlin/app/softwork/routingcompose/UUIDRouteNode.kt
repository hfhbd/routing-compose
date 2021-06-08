package app.softwork.routingcompose

import kotlinx.uuid.*

public class UUIDRouteNode : VariableRouteNode<UUID>() {
    override fun fromPath(directRoute: String): UUID? = directRoute.toUUIDOrNull()
}