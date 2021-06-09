package app.softwork.routingcompose

public abstract class VariableRouteNode<T : Any> : RouteNode() {
    public lateinit var variable: T

    override fun matches(subRoute: String): Boolean {
        val match = fromPath(subRoute)
        return when {
            match != null -> {
                variable = match
                true
            }
            else -> {
                false
            }
        }
    }

    public abstract fun fromPath(directRoute: String): T?
}