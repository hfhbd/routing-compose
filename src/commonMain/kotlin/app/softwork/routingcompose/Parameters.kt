package app.softwork.routingcompose

import kotlin.jvm.*

public class Parameters private constructor(public val raw: String, public val map: Map<String, List<String>>) {
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

        @JvmName("fromParameterList")
        public fun from(parameters: Map<String, List<String>>): Parameters {
            val raw = parameters.entries.flatMap { (key, values) ->
                values.mapNotNull {
                    if (it.isEmpty()) null else "$key=$it"
                }
            }.joinToString(separator = "&", prefix = "?") {
                it.percentDecode()
            }
            return Parameters(raw, parameters)
        }

        public fun from(parameters: Map<String, String>): Parameters {
            val raw = parameters.mapNotNull { (key, value) ->
                if (value.isEmpty()) null else "$key=$value"
            }.joinToString(separator = "&") {
                it.percentDecode()
            }
            return Parameters(raw, parameters.mapValues { listOf(it.value) })
        }

        public fun from(vararg parameters: Pair<String, String>): Parameters = from(parameters.toMap())

        @JvmName("fromParameterListVararg")
        public fun from(vararg parameters: Pair<String, List<String>>): Parameters = from(parameters.toMap())

        private fun String.percentEncode(): String {
            var encoded = this
            for ((replaced, value) in reservedCharacters) {
                encoded = encoded.replace(replaced, value)
            }
            return encoded
        }

        private fun String.percentDecode(): String {
            var decoded = this
            for ((value, replacement) in reservedCharacters) {
                decoded = decoded.replace(value, replacement)
            }
            return decoded
        }
    }

    override fun toString(): String = raw

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Parameters

        if (raw != other.raw) return false

        return true
    }

    override fun hashCode(): Int = raw.hashCode()
}
