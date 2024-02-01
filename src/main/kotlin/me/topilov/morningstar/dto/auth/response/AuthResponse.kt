package me.topilov.morningstar.dto.auth.response

import me.topilov.morningstar.dto.user.response.GetUserResponse

data class AuthResponse(
    val user: GetUserResponse,
    val accessToken: String,
)