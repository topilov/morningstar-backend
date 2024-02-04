package me.topilov.morningstar.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import me.topilov.morningstar.dto.content.*
import me.topilov.morningstar.dto.content.response.DeleteContentResponse
import me.topilov.morningstar.entity.UserDetailsImpl
import me.topilov.morningstar.exception.content.NotHaveDeleteContentPermissionsException
import me.topilov.morningstar.service.ContentService
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ContentController(
    private val contentService: ContentService,
) {

    @PostMapping("/user/{userId}/contents", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createContent(
        @PathVariable("userId") userId: Long,
        @ModelAttribute createContentDto: CreateContentDto,
    ): BasicContentDto {
        return contentService.createContent(userId, createContentDto)
    }

    @PutMapping("/content/{contentId}")
    fun updateContent(
        @PathVariable("contentId") contentId: Long,
        @Valid @RequestBody updateContentDto: UpdateContentDto
    ): BasicContentDto {
        return contentService.updateContent(contentId, updateContentDto)
    }

    @GetMapping("/content/{contentId}")
    fun getContent(@PathVariable("contentId") contentId: Long): BasicContentDto {
        return contentService.findBasicContentById(contentId)
    }

    @GetMapping("/content/{contentId}/file")
    @Transactional
    fun getFileContent(@PathVariable("contentId") contentId: Long): FileContentDto {
        return contentService.findFileContentById(contentId)
    }

    @GetMapping("/content/{contentId}/image-preview")
    @Transactional
    fun getImagePreviewContent(@PathVariable("contentId") contentId: Long): ImagePreviewContentDto {
        return contentService.findImagePreviewContentById(contentId)
    }

    @GetMapping("/contents")
    fun getContents(): List<BasicContentDto> {
        return contentService.findAllBasicContent()
    }

    @DeleteMapping("/content/{contentId}")
    fun deleteContent(
        @PathVariable("contentId") contentId: Long,
        authentication: Authentication,
        request: HttpServletRequest
    ): DeleteContentResponse {
        val userDetails = authentication.principal as UserDetailsImpl
        val content = contentService.findContentById(contentId)

        if (content.owner?.id != userDetails.user.id) {
            throw NotHaveDeleteContentPermissionsException(contentId)
        }

        contentService.deleteContentById(contentId)

        return DeleteContentResponse(success = true)
    }
}