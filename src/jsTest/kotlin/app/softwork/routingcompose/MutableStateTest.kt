package app.softwork.routingcompose

import androidx.compose.runtime.*
import kotlin.test.*

class MutableStateTest {
    @Test
    fun withoutCompose() {
        var x by mutableStateOf(0)
        assertEquals(0, x)
        x = 42
        assertEquals(42, x)
    }

    @Test
    fun withoutComposeClass() {
        val f = Foo()
        assertEquals(0, f.x)
        f.x = 42
        assertEquals(42, f.x)
    }

    class Foo {
        var x by mutableStateOf(0)
    }
}