package me.topilov.morningstar.utils

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.topilov.morningstar.service.AuthTokenService
import me.topilov.morningstar.service.UserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

const val BEARER_PREFIX = "Bearer "
const val AUTH_HEADER_NAME = "Authorization"

@Component
class JwtAuthenticationFilter(
    private val authTokenService: AuthTokenService,
    private val userService: UserService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(AUTH_HEADER_NAME)

        if (authHeader.isNullOrEmpty() || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = authHeader.substring(BEARER_PREFIX.length)

        val username = authTokenService.extractUsername(accessToken)

        if (!username.isNullOrEmpty() && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userService.loadUserByUsername(username)

            if (authTokenService.isTokenValid(accessToken, username)) {
                val context = SecurityContextHolder.createEmptyContext()

                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities,
                )

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                context.authentication = authToken
                SecurityContextHolder.setContext(context)
            }
        }

        filterChain.doFilter(request, response)
    }
}