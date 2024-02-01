package me.topilov.morningstar.exception.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.topilov.morningstar.utils.writeError
import org.springframework.http.HttpStatus
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class AccessDeniedHandlerImpl : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: org.springframework.security.access.AccessDeniedException
    ) {
        response.writeError(HttpStatus.FORBIDDEN, accessDeniedException.message)
    }
}