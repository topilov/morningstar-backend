package me.topilov.morningstar.controller

import me.topilov.morningstar.entity.User
import me.topilov.morningstar.service.AuthenticationService
import me.topilov.morningstar.service.JwtService
import me.topilov.morningstar.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.math.log

@RestController
@RequestMapping("/auth")
class AuthenticationController(
   private val authenticationService: AuthenticationService,
) {

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<JwtAuthenticationResponse> {
        return authenticationService.register(registerRequest)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.badRequest().build()
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<JwtAuthenticationResponse> {
        return authenticationService.login(loginRequest)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.badRequest().build()
    }
}

data class LoginRequest(
    val username: String,
    val password: String,
)

data class RegisterRequest(
    val username: String,
    val password: String,
)

data class JwtAuthenticationResponse(
    val token: String,
)