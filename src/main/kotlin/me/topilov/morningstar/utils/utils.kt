package me.topilov.morningstar.utils

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import me.topilov.morningstar.exception.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.util.*

fun HttpServletResponse.writeError(status: HttpStatus, error: String?) {
    this.status = status.value()
    this.contentType = "application/json"
    this.characterEncoding = "UTF-8"

    val errors = Collections.singletonList(error)
    val errorsResponse = ErrorResponse(errors)
    val json = Jackson2ObjectMapperBuilder.json().build<ObjectMapper>().writeValueAsString(errorsResponse)

    writer.write(json)
}