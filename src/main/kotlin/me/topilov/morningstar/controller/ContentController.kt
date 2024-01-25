package me.topilov.morningstar.controller

import me.topilov.morningstar.service.ContentService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ContentController(@Qualifier("contentService") private val contentService: ContentService) {
    @GetMapping("/content/pictures")
    fun getPictures(): List<String> {
        return contentService.getPictures()
    }
}