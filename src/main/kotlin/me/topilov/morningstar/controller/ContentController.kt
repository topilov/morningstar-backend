package me.topilov.morningstar.controller

import jakarta.servlet.http.HttpServletRequest
import me.topilov.morningstar.entity.Content
import me.topilov.morningstar.service.AuthTokenService
import me.topilov.morningstar.service.ContentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ContentController(
    private val contentService: ContentService,
    private val authTokenService: AuthTokenService,
) {

    @PostMapping("/content")
    fun insertContent(content: Content): ResponseEntity<Content> {
        val insertedContent = contentService.insert(content)
        return ResponseEntity.ok(insertedContent)
    }

    @PutMapping("/content")
    fun saveContent(content: Content): ResponseEntity<Content> {
        val savedContent = contentService.save(content)
        return ResponseEntity.ok(savedContent)
    }

    @GetMapping("/content/{id}")
    fun getContent(@PathVariable id: String): ResponseEntity<Content> {
        return contentService.findById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/content")
    fun getContent(): ResponseEntity<List<Content>> {
        val content = contentService.findAll()
        return ResponseEntity.ok(content)
    }

    @DeleteMapping("/content/{id}")
    fun deleteContent(@PathVariable id: String, request: HttpServletRequest): ResponseEntity<Boolean> {
        val accessToken = authTokenService.getAccessToken(request)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val content = contentService.findById(id)
            ?: return ResponseEntity.notFound().build()

        val username = authTokenService.extractUsername(accessToken)

        if (content.ownerUsername != username) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        contentService.deleteById(id)

        return ResponseEntity.ok(true)
    }
}