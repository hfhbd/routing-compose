package app.softwork.routingcompose

import kotlinx.serialization.*

/**
 * ```kotlin
 * @Route("/foo/:bar")
 * data class Bar(val bar: Int)
 * ```
 */
@OptIn(ExperimentalSerializationApi::class)
@MetaSerializable
public annotation class Route(val route: String)
