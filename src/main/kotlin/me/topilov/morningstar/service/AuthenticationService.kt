package me.topilov.morningstar.service

import me.topilov.morningstar.controller.JwtAuthenticationResponse
import me.topilov.morningstar.controller.LoginRequest
import me.topilov.morningstar.controller.RegisterRequest
import me.topilov.morningstar.entity.User
import me.topilov.morningstar.entity.UserDetailsImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {

    fun register(registerRequest: RegisterRequest): JwtAuthenticationResponse? {
        if (userService.existsByUsername(registerRequest.username)) return null

        val user = User(
            username = registerRequest.username,
            password = registerRequest.password,
            role = "ROLE_USER"
        )

        val insertedUser = userService.insertUser(user)
        val userDetails = UserDetailsImpl(insertedUser)
        val jwt = jwtService.generateToken(userDetails)

        return JwtAuthenticationResponse(jwt)
    }

    fun login(loginRequest: LoginRequest): JwtAuthenticationResponse? {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password,
            )
        )

        val user = userService.loadUserByUsername(loginRequest.username)
        val jwt = jwtService.generateToken(user)

        return JwtAuthenticationResponse(jwt)
    }
}