package me.topilov.morningstar.exception.handler

import me.topilov.morningstar.exception.ApiException
import me.topilov.morningstar.exception.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.util.*

@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleError(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        val errors = Collections.singletonList(ex.message)
        val errorResponse = ErrorResponse(errors)
        return ResponseEntity.badRequest().body(errorResponse)
    }
}