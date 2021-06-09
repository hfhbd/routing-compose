package app.softwork.routingcompose

public abstract class VariableRouteNode<T : Any> : RouteNode() {
    public lateinit var variable: T

    override fun matches(subRoute: String): Boolean {
        println("\nVARIABLEROUTENODE $subRoute matches $this\n\n")
        val match = fromPath(subRoute)
        val result = when {
            match != null -> {
                variable = match
                true
            }
            else -> {
                false
            }
        }
        println("\n\nRESULT $result Variable $match\n")
        return result
    }

    public abstract fun fromPath(directRoute: String): T?
}