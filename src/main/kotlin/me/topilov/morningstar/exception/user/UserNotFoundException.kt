package me.topilov.morningstar.exception.user

import me.topilov.morningstar.exception.ApiException

class UserNotFoundByIdException(userId: Long) : ApiException("User with id $userId not found")

class UserNotFoundByUsernameException(username: String) : ApiException("User with username $username not found")