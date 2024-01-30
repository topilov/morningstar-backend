package me.topilov.morningstar.api.authentication

data class AuthToken(
    val access: String,
    val refresh: String,
)
