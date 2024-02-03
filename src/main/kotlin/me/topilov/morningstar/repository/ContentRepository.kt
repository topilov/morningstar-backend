package me.topilov.morningstar.repository

import me.topilov.morningstar.dto.content.BasicContentDto
import me.topilov.morningstar.dto.content.FileContentDto
import me.topilov.morningstar.dto.content.ImagePreviewContentDto
import me.topilov.morningstar.entity.Content
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository : JpaRepository<Content, Long> {
    fun findByOwnerId(ownerId: Long): List<Content>

    @Query("SELECT new me.topilov.morningstar.dto.content.BasicContentDto(c.id, c.title, c.description, c.price) from Content c")
    fun findAllBasic(): List<BasicContentDto>

    @Query("SELECT new me.topilov.morningstar.dto.content.BasicContentDto(c.id, c.title, c.description, c.price) from Content c where c.id = :id")
    fun findBasicById(@Param("id") id: Long): BasicContentDto

    @Query("SELECT new me.topilov.morningstar.dto.content.ImagePreviewContentDto(c.id, c.imagePreview, c.imagePreviewType) from Content c where c.id = :id")
    fun findImagePreviewById(@Param("id") id: Long): ImagePreviewContentDto

    @Query("SELECT new me.topilov.morningstar.dto.content.FileContentDto(c.id, c.file, c.fileType) from Content c where c.id = :id")
    fun findFileById(@Param("id") id: Long): FileContentDto
}