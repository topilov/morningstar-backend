package me.topilov.morningstar.exception.content

class NotHaveDeleteContentPermissionsException(contentId: Long) : RuntimeException("You can not delete content with id $contentId")
