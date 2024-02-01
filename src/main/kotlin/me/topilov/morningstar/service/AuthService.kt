package me.topilov.morningstar.service

import me.topilov.morningstar.dto.auth.AuthData
import me.topilov.morningstar.dto.auth.AuthToken
import me.topilov.morningstar.dto.auth.LoginDto
import me.topilov.morningstar.dto.auth.RegisterDto
import me.topilov.morningstar.exception.auth.InvalidRefreshTokenException
import me.topilov.morningstar.exception.auth.UsernameAlreadyTakenException
import me.topilov.morningstar.mapper.UserMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val authTokenService: AuthTokenService,
    private val authenticationManager: AuthenticationManager,
) {

    fun register(registerDto: RegisterDto): AuthData {
        if (userService.existsByUsername(registerDto.username))  {
            throw UsernameAlreadyTakenException()
        }

        val createdUser = userMapper.toCreateUserDto(registerDto).let(userService::createUser)
        val authToken = authTokenService.generateAuthToken(createdUser.username, createdUser.role)

        return AuthData(createdUser, authToken)
    }

    fun login(loginRequest: LoginDto): AuthData {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password,
            )
        )

        val user = userService.findUserByUsername(loginRequest.username)
        val authToken = authTokenService.generateAuthToken(user.username, user.role)

        return AuthData(user, authToken)
    }

    fun refreshToken(refreshToken: String, username: String): AuthToken {
        val user = userService.findUserByUsername(username)

        if (!authTokenService.isTokenValid(refreshToken, username)) {
            throw InvalidRefreshTokenException()
        }

        return authTokenService.generateAuthToken(username, user.role)
    }
}