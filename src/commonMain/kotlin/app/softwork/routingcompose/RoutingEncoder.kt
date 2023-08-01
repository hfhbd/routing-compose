package app.softwork.routingcompose

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
internal class RoutingEncoder(
    override val serializersModule: SerializersModule,
    val result: Appendable
) : AbstractEncoder() {
    override fun encodeNull() {
        super.encodeNull()
    }

    override fun encodeValue(value: Any) {
        super.encodeValue(value)
    }
}
