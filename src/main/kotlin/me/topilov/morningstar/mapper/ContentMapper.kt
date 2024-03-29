package me.topilov.morningstar.mapper

import me.topilov.morningstar.dto.content.BasicContentDto
import me.topilov.morningstar.dto.content.CreateContentDto
import me.topilov.morningstar.dto.content.UpdateContentDto
import me.topilov.morningstar.entity.Content
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ContentMapper {
    fun toContent(createContentDto: CreateContentDto): Content
    fun toContent(updateContentDto: UpdateContentDto): Content
    fun toBasicContentDto(content: Content): BasicContentDto
}