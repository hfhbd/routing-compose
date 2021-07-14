package app.softwork.routingcompose

public sealed class Node {
    public abstract fun matches(subRoute: String): Boolean
}
