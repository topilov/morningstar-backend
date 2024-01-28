package me.topilov.morningstar.entity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document(collection = "users")
data class User(
    @Id
    var id: String? = null,

    @get: NotBlank
    @get: Size(max = 16)
    var username: String,

    @get: NotBlank
    @get: Size(max = 120)
    var password: String,

    var role: String,
    val balance: Int = 0,
)
