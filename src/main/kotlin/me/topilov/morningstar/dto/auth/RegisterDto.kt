package me.topilov.morningstar.dto.auth

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class RegisterDto(
    @field:NotEmpty(message = "Username can not be empty")
    @field:Size(min = 4, max = 16, message = "Username length must be from 4 to 16 chars")
    var username: String,
    @field:NotEmpty(message = "Password can not be empty")
    @field:Size(min = 8, max = 64, message = "Password length must be from 8 to 64 chars")
    var password: String,
)
