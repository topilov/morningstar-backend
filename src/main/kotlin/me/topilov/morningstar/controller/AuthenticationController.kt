package me.topilov.morningstar.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.topilov.morningstar.api.authentication.AccessTokenResponse
import me.topilov.morningstar.api.authentication.AuthResponse
import me.topilov.morningstar.api.authentication.LoginRequest
import me.topilov.morningstar.api.authentication.RegisterRequest
import me.topilov.morningstar.service.AuthTokenService
import me.topilov.morningstar.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val authTokenService: AuthTokenService,
) {

    @PostMapping("/register")
    fun register(
        @RequestBody registerRequest: RegisterRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val authData = authenticationService.register(registerRequest) ?: return ResponseEntity.badRequest().build()
        val cookie = authTokenService.getRefreshTokenCookie(authData.token.refresh)

        response.addCookie(cookie)

        val authResponse = AuthResponse(authData.user, authData.token.access)

        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val authData = authenticationService.login(loginRequest) ?: return ResponseEntity.badRequest().build()
        val cookie = authTokenService.getRefreshTokenCookie(authData.token.refresh)

        response.addCookie(cookie)

        val authResponse = AuthResponse(authData.user, authData.token.access)

        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/refresh")
    fun refreshToken(@CookieValue("refresh_token", required = false) refreshToken: String?, response: HttpServletResponse): ResponseEntity<AccessTokenResponse> {
        val authToken = refreshToken?.let(authenticationService::refreshToken)
            ?: return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .build()

        val cookie = authTokenService.getRefreshTokenCookie(authToken.refresh)

        response.addCookie(cookie)

        val authResponse = AccessTokenResponse(authToken.access)

        return ResponseEntity.ok(authResponse)
    }
}