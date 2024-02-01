package me.topilov.morningstar.exception.auth

import me.topilov.morningstar.exception.ApiException

class UsernameAlreadyTakenException : ApiException("Username already taken")