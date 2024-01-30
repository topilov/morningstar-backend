package me.topilov.morningstar.api.authentication

data class LoginRequest(
    val username: String,
    val password: String,
)