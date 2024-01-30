package me.topilov.morningstar.service

import me.topilov.morningstar.api.authentication.AuthData
import me.topilov.morningstar.api.authentication.AuthToken
import me.topilov.morningstar.api.authentication.LoginRequest
import me.topilov.morningstar.api.authentication.RegisterRequest
import me.topilov.morningstar.entity.User
import me.topilov.morningstar.entity.UserDetailsImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userService: UserService,
    private val authTokenService: AuthTokenService,
    private val authenticationManager: AuthenticationManager,
) {

    fun register(registerRequest: RegisterRequest): AuthData? {
        if (userService.existsByUsername(registerRequest.username)) return null

        val user = User(
            username = registerRequest.username,
            password = registerRequest.password,
            role = "ROLE_USER"
        )

        val insertedUser = userService.insertUser(user)
        val userDetails = UserDetailsImpl(insertedUser)
        val authToken = authTokenService.generateAuthToken(userDetails)

        return AuthData(user, authToken)
    }

    fun login(loginRequest: LoginRequest): AuthData? {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password,
            )
        )

        val userDetails = userService.loadUserByUsername(loginRequest.username)
        val authToken = authTokenService.generateAuthToken(userDetails)

        return AuthData(userDetails.user, authToken)
    }

    fun refreshToken(refreshToken: String): AuthToken? {
        val username = authTokenService.extractUsername(refreshToken) ?: return null
        val userDetails = userService.loadUserByUsername(username)

        if (authTokenService.isTokenValid(refreshToken, userDetails)) {
            return authTokenService.generateAuthToken(userDetails)
        }

        return null
    }
}