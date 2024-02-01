package me.topilov.morningstar.exception.content

import me.topilov.morningstar.exception.ApiException

class NotHaveDeleteContentPermissionsException(contentId: Long) : ApiException("You can not delete content with id $contentId")
