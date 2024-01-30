package me.topilov.morningstar.api.authentication

data class RegisterRequest(
    val username: String,
    val password: String,
)