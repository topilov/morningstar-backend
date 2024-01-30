package me.topilov.morningstar.api.authentication

import me.topilov.morningstar.entity.User

data class AuthData(
    val user: User,
    val token: AuthToken,
)
