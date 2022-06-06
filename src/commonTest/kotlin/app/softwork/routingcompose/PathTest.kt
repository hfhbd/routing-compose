package app.softwork.routingcompose

import kotlin.test.*

class PathTest {
    @Test
    fun testingPathConverter() {
        val prefixPath = "/a"
        assertEquals(Path("/a", null), Path.from(prefixPath))
        assertEquals(Path("/a", null), Path.from("a"))
    }

    @Test
    fun relative() {
        val base = Path.from("/a/b/c/d")
        assertEquals("/a/b/c", base.relative("./").path)
        assertEquals("/a/b", base.relative("././").path)
        assertEquals("/a/b/g", base.relative("././g").path)

        assertEquals("/a/b/g?foo=bar", base.relative("././g?foo=bar").toString())
    }
}
