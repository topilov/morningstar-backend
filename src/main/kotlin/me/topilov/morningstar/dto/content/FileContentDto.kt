package me.topilov.morningstar.dto.content

data class FileContentDto(
    val id: Long,
    val file: ByteArray,
    val fileType: String,
)
