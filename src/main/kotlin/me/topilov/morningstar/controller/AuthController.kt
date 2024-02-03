package me.topilov.morningstar.controller

import com.fasterxml.jackson.annotation.JsonView
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import me.topilov.morningstar.dto.auth.LoginDto
import me.topilov.morningstar.dto.auth.RegisterDto
import me.topilov.morningstar.dto.auth.response.AccessTokenResponse
import me.topilov.morningstar.dto.auth.response.AuthResponse
import me.topilov.morningstar.exception.auth.InvalidRefreshTokenException
import me.topilov.morningstar.service.AuthService
import me.topilov.morningstar.service.AuthTokenService
import me.topilov.morningstar.utils.View
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/auth")
class AuthController(
    private val authService: AuthService,
    private val authTokenService: AuthTokenService,
) {

    @PostMapping("/register")
    @JsonView(View.AuthenticatedUser::class)
    fun register(
        @Valid @RequestBody registerDto: RegisterDto,
        response: HttpServletResponse
    ): AuthResponse {
        val authData = authService.register(registerDto)
        val cookie = authTokenService.getRefreshTokenCookie(authData.token.refresh)

        response.addCookie(cookie)

        return AuthResponse(authData.user, authData.token.access)
    }

    @PostMapping("/login")
    @JsonView(View.AuthenticatedUser::class)
    fun login(
        @Valid @RequestBody loginDto: LoginDto,
        response: HttpServletResponse
    ): AuthResponse {
        val authData = authService.login(loginDto)
        val cookie = authTokenService.getRefreshTokenCookie(authData.token.refresh)

        response.addCookie(cookie)

        return AuthResponse(authData.user, authData.token.access)
    }

    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue("refresh_token", required = false) refreshToken: String?,
        response: HttpServletResponse
    ): AccessTokenResponse {
        if (refreshToken == null) {
            throw InvalidRefreshTokenException()
        }

        val authToken = authService.refreshToken(refreshToken)

        val cookie = authTokenService.getRefreshTokenCookie(authToken.refresh)

        response.addCookie(cookie)

        return AccessTokenResponse(authToken.access)
    }
}