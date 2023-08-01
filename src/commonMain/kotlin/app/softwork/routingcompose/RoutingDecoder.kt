package app.softwork.routingcompose

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
internal class RoutingDecoder(
    override val serializersModule: SerializersModule,
    private val elements: Array<Any?>
) : AbstractDecoder() {
    private var currentIndex = 0
    override fun decodeSequentially(): Boolean = true

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (currentIndex == elements.lastIndex) {
            return CompositeDecoder.DECODE_DONE
        }
        return currentIndex
    }

    override fun decodeValue(): Any = requireNotNull(elements[currentIndex++]) {
        "REQUIRED NOT NULL ${currentIndex - 1} with ${elements.joinToString()}"
    }
}
