package app.softwork.routingcompose

import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Route("/users/:userID")
data class Users(val userID: Int)

@ExperimentalSerializationApi
@Route("/users/:user/todos/:todoID")
data class Todos(val userID: Int, val todoID: Int)

@ExperimentalSerializationApi
@Route("/:user/todos/:todoID")
data class UserTodo(val users: Users, val todoID: Int)
