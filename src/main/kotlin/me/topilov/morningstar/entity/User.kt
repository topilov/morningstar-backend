package me.topilov.morningstar.entity

import com.fasterxml.jackson.annotation.JsonView
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import me.topilov.morningstar.utils.View
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    var id: String? = null,

    @get: NotBlank
    @get: Size(max = 16)
    var username: String = "",

    @get: NotBlank
    @get: Size(max = 120)
    @field:JsonView(View.Admin::class)
    var password: String,

    var role: String = "ROLE_USER",

    @field:JsonView(View.AuthenticatedUser::class, View.Admin::class)
    val balance: Int = 0,

    val isLocked: Boolean = false,
)
