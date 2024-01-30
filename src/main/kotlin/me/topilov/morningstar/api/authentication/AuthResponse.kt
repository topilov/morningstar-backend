package me.topilov.morningstar.api.authentication

import me.topilov.morningstar.entity.User

data class AuthResponse(
    val user: User,
    val token: String,
)