package me.topilov.morningstar.exception.auth

import me.topilov.morningstar.exception.ApiException

class InvalidRefreshTokenException : ApiException("Invalid refresh token")