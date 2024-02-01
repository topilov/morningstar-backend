package me.topilov.morningstar.dto.auth

data class AuthToken(
    val access: String,
    val refresh: String,
)
