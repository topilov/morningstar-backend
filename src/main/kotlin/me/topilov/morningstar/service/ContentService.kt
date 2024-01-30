package me.topilov.morningstar.service

import me.topilov.morningstar.entity.Content
import me.topilov.morningstar.repository.ContentRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ContentService(private val contentRepository: ContentRepository) {

    fun findById(id: String): Content? {
        return contentRepository.findById(id).getOrNull()
    }

    fun findAll(): List<Content> {
        return contentRepository.findAll()
    }

    fun deleteById(id: String) {
        return contentRepository.deleteById(id)
    }

    fun save(content: Content): Content {
        return contentRepository.save(content)
    }

    fun insert(content: Content): Content {
        return contentRepository.insert(content)
    }
}