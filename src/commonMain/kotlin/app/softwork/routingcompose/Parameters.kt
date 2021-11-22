package app.softwork.routingcompose

public data class Parameters(val raw: String, val map: Map<String, List<String>>) {
    public companion object {
        private val reservedCharacters = mapOf(
            "+" to " ",
            "%20" to " ",
            "%21" to "!",
            "%22" to "\"",
            "%23" to "#",
            "%24" to "$",
            "%25" to "%",
            "%26" to "&",
            "%27" to "'",
            "%28" to "(",
            "%29" to ")",
            "%2A" to "*",
            "%2B" to "+",
            "%2C" to ",",
            "%2D" to "-",
            "%2E" to ".",
            "%2F" to "/",

            "%3A" to ":",
            "%3B" to ";",
            "%3C" to "<",
            "%3D" to "=",
            "%3E" to ">",
            "%3F" to "?",

            "%40" to "@",

            "%5B" to "[",
            "%5C" to "\\",
            "%5D" to "]",
        )

        public fun from(rawParameters: String): Parameters {
            val parameters = rawParameters.split("&", ";")
            val keyed: Map<String, List<String>> = parameters
                .map { it.split("=") }
                .groupBy({ it.first() }) { it.last() }
                .mapValues {
                    it.value.filter { it.isNotEmpty() }
                }.mapValues { (_, values) ->
                    values.map { it.percentEncode() }
                }.filter { it.value.isNotEmpty() }

            return Parameters(rawParameters, keyed)
        }

        private fun String.percentEncode(): String {
            var encoded = this
            for ((replaced, value) in reservedCharacters) {
                encoded = encoded.replace(replaced, value)
            }
            return encoded
        }
    }
}
