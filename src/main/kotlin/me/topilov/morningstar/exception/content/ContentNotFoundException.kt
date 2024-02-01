package me.topilov.morningstar.exception.content

class ContentNotFoundException(contentId: Long) : RuntimeException("Content with id $contentId not found")