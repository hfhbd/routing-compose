package app.softwork.routingcompose

import org.jetbrains.compose.web.dom.*
import kotlin.test.*

internal class HashRouterTest {

    @Test
    fun simpleTest() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                route("/foo") {
                    noMatch("foo") {
                        Text("foo")
                    }
                }
                route("/bar") {
                    noMatch("bar") {
                        Text("bar")
                    }
                }
                noMatch("other") {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)

        router.navigate("/foo","foo")
        router.navigate("/bar", "bar")
    }

    @Test
    fun emptyTest() = runTest {
        val router = MockRouter()
        compose {
            router("/") {
                noMatch("other") {
                    Text("other")
                }
            }
        }
        assertEquals("other", root.innerHTML)
    }

    @Test
    fun deepTest() = runTest {
        val router = MockRouter()
        compose {
            router("/foo") {
                route("/foo") {
                    route("/bar") {
                        route("/baz"){
                            noMatch("baz") {
                                Text("baz")
                            }
                        }
                        noMatch("bar") {
                            Text("bar")
                        }
                    }
                    noMatch("foo") {
                        Text("foo")
                    }
                }
                noMatch("other") {
                    Text("other")
                }
            }
        }
        assertEquals("foo", root.innerHTML)
        router.navigate("/foo/bar", "bar")
        router.navigate("/foo/bar/baz", "baz")
        router.navigate("/", "other")
    }
}
