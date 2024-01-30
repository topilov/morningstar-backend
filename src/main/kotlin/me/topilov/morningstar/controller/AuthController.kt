package me.topilov.morningstar.controller

import com.fasterxml.jackson.annotation.JsonView
import jakarta.servlet.http.HttpServletResponse
import me.topilov.morningstar.api.authentication.AccessTokenResponse
import me.topilov.morningstar.api.authentication.AuthResponse
import me.topilov.morningstar.api.authentication.LoginRequest
import me.topilov.morningstar.api.authentication.RegisterRequest
import me.topilov.morningstar.service.AuthService
import me.topilov.morningstar.service.AuthTokenService
import me.topilov.morningstar.utils.View
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/auth")
class AuthController(
    private val authService: AuthService,
    private val authTokenService: AuthTokenService,
) {

    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val authData = authService.register(registerRequest) ?: return ResponseEntity.badRequest().build()
        val cookie = authTokenService.getRefreshTokenCookie(authData.token.refresh)

        response.addCookie(cookie)

        val authResponse = AuthResponse(authData.user, authData.token.access)

        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/login")
    @JsonView(View.GuestUser::class)
    fun login(
        @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val authData = authService.login(loginRequest) ?: return ResponseEntity.badRequest().build()
        val cookie = authTokenService.getRefreshTokenCookie(authData.token.refresh)

        response.addCookie(cookie)

        val authResponse = AuthResponse(authData.user, authData.token.access)

        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/refresh")
    fun refreshToken(@CookieValue("refresh_token", required = false) refreshToken: String?, response: HttpServletResponse): ResponseEntity<AccessTokenResponse> {
        val authToken = refreshToken?.let(authService::refreshToken)
            ?: return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()

        val cookie = authTokenService.getRefreshTokenCookie(authToken.refresh)

        response.addCookie(cookie)

        val authResponse = AccessTokenResponse(authToken.access)

        return ResponseEntity.ok(authResponse)
    }
}