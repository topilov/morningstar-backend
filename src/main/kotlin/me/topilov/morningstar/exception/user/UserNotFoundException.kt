package me.topilov.morningstar.exception.user

open class UserNotFoundException(message: String) : RuntimeException(message)

class UserNotFoundByIdException(userId: Long) : UserNotFoundException("User with id $userId not found")

class UserNotFoundByUsernameException(username: String) :
    UserNotFoundException("User with username $username not found")