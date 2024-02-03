package me.topilov.morningstar.service

import me.topilov.morningstar.dto.content.*
import me.topilov.morningstar.entity.Content
import me.topilov.morningstar.exception.content.ContentNotFoundException
import me.topilov.morningstar.mapper.ContentMapper
import me.topilov.morningstar.mapper.UserMapper
import me.topilov.morningstar.repository.ContentRepository
import org.apache.tika.Tika
import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class ContentService(
    private val contentRepository: ContentRepository,
    private val contentMapper: ContentMapper,
    private val userService: UserService,
    private val userMapper: UserMapper,
) {

    fun findContentById(contentId: Long): Content {
        return contentRepository.findById(contentId)
            .orElseThrow { ContentNotFoundException(contentId) }
    }

    fun findBasicContentById(contentId: Long): BasicContentDto {
        return contentRepository.findBasicById(contentId)
    }

    fun findFileContentById(contentId: Long): FileContentDto {
        return contentRepository.findFileById(contentId)
    }

    fun findImagePreviewContentById(contentId: Long): ImagePreviewContentDto {
        return contentRepository.findImagePreviewById(contentId)
    }

    fun findAllBasicContent(): List<BasicContentDto> {
        return contentRepository.findAllBasic()
    }

    fun deleteContentById(id: Long) {
        return contentRepository.deleteById(id)
    }

    fun createContent(userId: Long, createContentDto: CreateContentDto): BasicContentDto {
        val user = userService.findUserById(userId)
            .let(userMapper::toUser)

        createContentDto.owner = user

        val content = contentMapper.toContent(createContentDto)
        val file = createContentDto.multipartFile

        val fileBytes: ByteArray = file.bytes
        content.file = fileBytes
        content.fileType = file.contentType.toString()

        val tika = Tika()
        val mimeType = tika.detect(fileBytes)

        if (mimeType.startsWith("video/")) {
            val frameGrabber = FFmpegFrameGrabber(ByteArrayInputStream(fileBytes))
            frameGrabber.start()
            val frame = frameGrabber.grabKeyFrame()
            val converter = Java2DFrameConverter()
            val bufferedImage = converter.convert(frame)
            val previewBytes = ByteArrayOutputStream()
            ImageIO.write(bufferedImage, "png", previewBytes)
            frameGrabber.stop()

            content.imagePreview = previewBytes.toByteArray()
            content.imagePreviewType = "image/png"
        }

        return contentRepository.saveAndFlush(content)
            .let(contentMapper::toBasicContentDto)
    }

    fun updateContent(contentId: Long, updateContentDto: UpdateContentDto): BasicContentDto {
        val getContentResponse = findContentById(contentId)

        updateContentDto.id = contentId
        updateContentDto.owner = getContentResponse.owner

        val content = contentMapper.toContent(updateContentDto)

        return contentRepository.save(content)
            .let(contentMapper::toBasicContentDto)
    }
}