package me.topilov.morningstar.dto.content

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import me.topilov.morningstar.entity.User
import org.hibernate.validator.constraints.Length

data class UpdateContentDto(
    var owner: User? = null,
    var id: Long? = null,

    @field:NotEmpty
    @field:Size(max = 64)
    var title: String,

    @field:NotEmpty
    @field:Length(max = 512)
    var description: String,

    @field:NotNull
    @field:Positive
    var price: Double,
)
