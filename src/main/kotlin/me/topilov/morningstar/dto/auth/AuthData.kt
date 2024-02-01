package me.topilov.morningstar.dto.auth

import me.topilov.morningstar.dto.user.response.GetUserResponse

data class AuthData(
    val user: GetUserResponse,
    val token: AuthToken,
)