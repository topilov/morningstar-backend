package me.topilov.morningstar.dto.user

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class UpdateUserDto(
    var id: Long,

    @field:NotEmpty(message = "Username can not be empty")
    @field:Size(min = 4, max = 16, message = "Username length must be from 4 to 16 chars")
    var username: String,

    @field:NotEmpty(message = "Password can not be empty")
    @field:Size(min = 8, max = 64, message = "Password length must be from 8 to 64 chars")
    var password: String,

    var role: String,

    @field:Positive(message = "Balance should be positive value")
    var balance: Double,
    var isLocked: Boolean,
)
