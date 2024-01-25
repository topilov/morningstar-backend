package me.topilov.morningstar.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ContentService {

    @Value("\${content.api.url}")
    private val apiUrl: String? = null

    fun getPictures(): List<String> {
        return emptyList()
    }
}