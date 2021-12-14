package app.softwork.routingcompose

import kotlin.test.*

class PathTest {
    @Test
    fun testingPathConverter() {
        val prefixPath = "/a"
        assertEquals(Path("/a", null), Path.from(prefixPath))
        assertEquals(Path("/a", null), Path.from("a"))
    }
}
