package me.topilov.morningstar.entity

import com.fasterxml.jackson.annotation.JsonView
import me.topilov.morningstar.utils.View
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "content")
data class Content(
    @Id
    var id: String? = null,

    var ownerUsername: String = "",

    var title: String,

    var description: String = "",

    var price: Double = 0.0,

    @field:JsonView(View.Admin::class)
    var path: String? = null,
)
