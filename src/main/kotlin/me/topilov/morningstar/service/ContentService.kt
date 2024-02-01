package me.topilov.morningstar.service

import me.topilov.morningstar.dto.content.CreateContentDto
import me.topilov.morningstar.dto.content.UpdateContentDto
import me.topilov.morningstar.dto.content.response.GetContentResponse
import me.topilov.morningstar.dto.content.response.GetContentsResponse
import me.topilov.morningstar.exception.content.ContentNotFoundException
import me.topilov.morningstar.mapper.ContentMapper
import me.topilov.morningstar.mapper.UserMapper
import me.topilov.morningstar.repository.ContentRepository
import org.springframework.stereotype.Service

@Service
class ContentService(
    private val contentRepository: ContentRepository,
    private val contentMapper: ContentMapper,
    private val userService: UserService,
    private val userMapper: UserMapper,
) {

    fun findContentById(id: Long): GetContentResponse {
        return contentRepository.findById(id)
            .map(contentMapper::toGetContentResponse)
            .orElseThrow { ContentNotFoundException(id) }
    }

    fun findAllContent(): GetContentsResponse {
        return contentRepository.findAll()
            .map(contentMapper::toGetContentResponse)
            .let(::GetContentsResponse)
    }

    fun deleteContentById(id: Long) {
        return contentRepository.deleteById(id)
    }

    fun createContent(userId: Long, createContentDto: CreateContentDto): GetContentResponse {
        val user = userService.findUserById(userId)
            .let(userMapper::toUser)

        createContentDto.owner = user

        val content = contentMapper.toContent(createContentDto)

        return contentRepository.saveAndFlush(content)
            .let(contentMapper::toGetContentResponse)
    }

    fun updateContent(contentId: Long, updateContentDto: UpdateContentDto): GetContentResponse {
        val getContentResponse = findContentById(contentId)

        updateContentDto.id = contentId
        updateContentDto.owner = getContentResponse.owner.let(userMapper::toUser)

        val content = contentMapper.toContent(updateContentDto)

        return contentRepository.save(content)
            .let(contentMapper::toGetContentResponse)
    }
}