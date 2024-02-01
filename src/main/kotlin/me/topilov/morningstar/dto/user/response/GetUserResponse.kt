package me.topilov.morningstar.dto.user.response

import com.fasterxml.jackson.annotation.JsonView
import me.topilov.morningstar.utils.View
import java.time.LocalDateTime

data class GetUserResponse(
    var id: Long,

    var username: String,

    @field:JsonView(View.Admin::class)
    var password: String,

    var role: String,

    @field:JsonView(View.AuthenticatedUser::class)
    var balance: Double,

    var isLocked: Boolean,

    var createdAt: LocalDateTime?,

    var updatedAt: LocalDateTime?,
)