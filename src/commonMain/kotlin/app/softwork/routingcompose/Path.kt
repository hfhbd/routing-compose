package app.softwork.routingcompose

public data class Path(val path: String, val parameters: Parameters?) {
    internal fun newPath(currentPath: String) = Path(path = path.removePrefix("/$currentPath"), parameters)

    /**
     * https://datatracker.ietf.org/doc/html/rfc1808
     */
    internal fun relative(to: String): Path {
        val paths = path.split("/")
        val split = to.split("./")
        val result = split.last().let {
            if (it.isNotEmpty()) "/$it" else it
        }
        val number = split.count() - 1
        return from(
            paths.dropLast(number).joinToString(postfix = result, separator = "/") {
                it
            }
        )
    }

    public companion object {
        public fun from(rawPath: String): Path {
            val pathAndQuery = rawPath.split("?")
            val (path, rawParameters) = when (pathAndQuery.size) {
                1 -> {
                    pathAndQuery.first() to null
                }

                2 -> {
                    pathAndQuery.first() to pathAndQuery.last().let { Parameters.from(it) }
                }

                else -> {
                    error("path contains more than 1 '?' delimiter: $rawPath")
                }
            }
            return Path(path.addPrefix("/"), rawParameters)
        }

        private fun String.addPrefix(prefix: String) = if (startsWith(prefix)) this else "$prefix$this"
    }

    override fun toString(): String = if (parameters == null) {
        path
    } else {
        "$path?$parameters"
    }

    internal val currentPath get() = path.removePrefix("/").takeWhile { it != '/' }
}
