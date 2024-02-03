package me.topilov.morningstar.dto.content

data class ImagePreviewContentDto(
    val id: Long,
    val imagePreview: ByteArray,
    val imagePreviewType: String
)