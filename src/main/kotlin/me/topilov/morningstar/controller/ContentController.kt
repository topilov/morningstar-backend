package me.topilov.morningstar.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import me.topilov.morningstar.dto.content.CreateContentDto
import me.topilov.morningstar.dto.content.UpdateContentDto
import me.topilov.morningstar.dto.content.response.DeleteContentResponse
import me.topilov.morningstar.dto.content.response.GetContentResponse
import me.topilov.morningstar.dto.content.response.GetContentsResponse
import me.topilov.morningstar.entity.UserDetailsImpl
import me.topilov.morningstar.exception.content.NotHaveDeleteContentPermissionsException
import me.topilov.morningstar.service.ContentService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ContentController(
    private val contentService: ContentService,
) {

    @PostMapping("/user/{userId}/content")
    fun createContent(
        @PathVariable("userId") userId: Long,
        @Valid @RequestBody createContentDto: CreateContentDto
    ): GetContentResponse {
        return contentService.createContent(userId, createContentDto)
    }

    @PutMapping("/content/{contentId}")
    fun updateContent(
        @PathVariable("contentId") contentId: Long,
        @Valid @RequestBody updateContentDto: UpdateContentDto
    ): GetContentResponse {
        return contentService.updateContent(contentId, updateContentDto)
    }

    @GetMapping("/content/{contentId}")
    fun getContent(@PathVariable("contentId") contentId: Long): GetContentResponse {
        return contentService.findContentById(contentId)
    }

    @GetMapping("/content")
    fun getAllContent(): GetContentsResponse {
        return contentService.findAllContent()
    }

    @DeleteMapping("/content/{contentId}")
    fun deleteContent(
        @PathVariable("contentId") contentId: Long,
        authentication: Authentication,
        request: HttpServletRequest
    ): DeleteContentResponse {
        val userDetails = authentication.principal as UserDetailsImpl
        val content = contentService.findContentById(contentId)

        if (content.owner.id != userDetails.user.id) {
            throw NotHaveDeleteContentPermissionsException(contentId)
        }

        contentService.deleteContentById(contentId)

        return DeleteContentResponse(success = true)
    }
}