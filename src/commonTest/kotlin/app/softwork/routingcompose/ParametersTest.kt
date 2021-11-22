package app.softwork.routingcompose

import kotlin.test.*

class ParametersTest {
    @Test
    fun simpleTest() {
        val parameters = Parameters.from("key=value")
        assertEquals("key=value", parameters.raw)
        assertEquals(mapOf("key" to listOf("value")), parameters.map)
    }

    @Test
    fun listTest() {
        val parameters = Parameters.from("key=value&key=value1")
        assertEquals("key=value&key=value1", parameters.raw)
        assertEquals(mapOf("key" to listOf("value", "value1")), parameters.map)
    }

    @Test
    fun simpleMultipleTest() {
        val parameters = Parameters.from("key=value&key2=value2")
        assertEquals("key=value&key2=value2", parameters.raw)
        assertEquals(mapOf("key" to listOf("value"), "key2" to listOf("value2")), parameters.map)
    }

    @Test
    fun listMultipleTest() {
        val parameters = Parameters.from("key=value&key2=value2&key=value1&key2=value2")
        assertEquals("key=value&key2=value2&key=value1&key2=value2", parameters.raw)
        assertEquals(mapOf("key" to listOf("value", "value1"), "key2" to listOf("value2", "value2")), parameters.map)
    }

    @Test
    fun emptyTest() {
        val parameters = Parameters.from("")
        assertEquals("", parameters.raw)
        assertEquals(emptyMap(), parameters.map)
    }

    @Test
    fun listMultipleTestEncoding() {
        val parameters = Parameters.from("key=va%26lue&key2=val%20ue2&key=val+ue1;key2=val%3Due2")
        assertEquals("key=va%26lue&key2=val%20ue2&key=val+ue1;key2=val%3Due2", parameters.raw)
        assertEquals(mapOf("key" to listOf("va&lue", "val ue1"), "key2" to listOf("val ue2", "val=ue2")), parameters.map)
    }
}
