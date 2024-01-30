package me.topilov.morningstar.config

import me.topilov.morningstar.utils.View
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice

@RestControllerAdvice
class SecurityJsonViewControllerAdvice : AbstractMappingJacksonResponseBodyAdvice() {

    override fun beforeBodyWriteInternal(
        bodyContainer: MappingJacksonValue,
        contentType: MediaType,
        returnType: MethodParameter,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ) {
        bodyContainer.serializationView = View.GuestUser::class.java

        val authentication = SecurityContextHolder.getContext().authentication ?: return
        val authorities = authentication.authorities
        val role = authorities.first()

        val view = when (role.authority) {
            "ROLE_ANONYMOUS" -> View.GuestUser::class.java
            "ROLE_USER" -> View.AuthenticatedUser::class.java
            "ROLE_ADMIN" -> View.Admin::class.java
            else -> View.GuestUser::class.java
        }

        bodyContainer.serializationView = view
    }
}