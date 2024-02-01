package me.topilov.morningstar.dto.content.response

import com.fasterxml.jackson.annotation.JsonView
import me.topilov.morningstar.dto.user.response.GetUserResponse
import me.topilov.morningstar.utils.View

data class GetContentResponse(
    var id: Long,
    var title: String,
    var description: String,
    var price: Double,
    @field:JsonView(View.Admin::class)
    var path: String?,
    var owner: GetUserResponse,
)
