package me.topilov.morningstar.exception.content

import me.topilov.morningstar.exception.ApiException

class ContentNotFoundException(contentId: Long) : ApiException("Content with id $contentId not found")