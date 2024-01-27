package me.topilov.morningstar.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id val id: Int? = null,
    val username: String,
    val balance: Int,
)
