package app.softwork.routingcompose

/**
 * A [RouteNode] containing a lateinit [variable], which will be set, when the requested subroute [matches] this type by
 * calling [variable].
 *
 * With [NavBuilder] the corresponding implemementation provides a [Lazy] parameter to access the lateinit [variable].
 */
public abstract class VariableRouteNode<T : Any> : RouteNode() {
    public lateinit var variable: T

    override fun matches(subRoute: String): Boolean {
        val match = variable(fromRoute = subRoute)
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

    public abstract fun variable(fromRoute: String): T?
}