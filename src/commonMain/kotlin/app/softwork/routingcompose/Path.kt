package app.softwork.routingcompose

internal data class Path(val path: String, val parameters: Parameters?) {
    fun newPath(currentPath: String) = Path(path = path.removePrefix("/$currentPath"), parameters)

    internal companion object {
        fun from(rawPath: String): Path {
            require(rawPath.startsWith("/")) { "path must start with a slash: $rawPath" }
            val pathAndQuery = rawPath.split("?")
            val (path, rawParameters) = when (pathAndQuery.size) {
                1 -> {
                    pathAndQuery.first() to null
                }
                2 -> {
                    pathAndQuery.first() to pathAndQuery.last()
                }
                else -> {
                    error("path contains more than 1 '?' delimiter: $rawPath")
                }
            }
            return Path(path, rawParameters?.let { Parameters.from(it) })
        }
    }

    val currentPath get() = path.removePrefix("/").takeWhile { it != '/' }
}
