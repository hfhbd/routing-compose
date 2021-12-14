package app.softwork.routingcompose

internal data class Path(val path: String, val parameters: Parameters?) {
    fun newPath(currentPath: String) = Path(path = path.removePrefix("/$currentPath"), parameters)

    internal companion object {
        fun from(rawPath: String): Path {
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
            return Path(path.addPrefix("/"), rawParameters?.let { Parameters.from(it) })
        }

        private fun String.addPrefix(prefix: String) = if(startsWith(prefix)) this else "$prefix$this"
    }

    val currentPath get() = path.removePrefix("/").takeWhile { it != '/' }
}
