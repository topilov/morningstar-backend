package me.topilov.morningstar.service

import me.topilov.morningstar.dto.user.CreateUserDto
import me.topilov.morningstar.dto.user.UpdateUserDto
import me.topilov.morningstar.dto.user.response.DeleteUserResponse
import me.topilov.morningstar.dto.user.response.GetUserResponse
import me.topilov.morningstar.dto.user.response.GetUsersResponse
import me.topilov.morningstar.entity.UserDetailsImpl
import me.topilov.morningstar.exception.user.UserNotFoundByIdException
import me.topilov.morningstar.exception.user.UserNotFoundByUsernameException
import me.topilov.morningstar.mapper.UserMapper
import me.topilov.morningstar.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : UserDetailsService {

    @Autowired
    private lateinit var bCryptPasswordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String): UserDetailsImpl {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundByUsernameException(username)
        return UserDetailsImpl(user)
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }

    fun findUserByUsername(username: String): GetUserResponse {
        return userRepository.findByUsername(username)
            ?.let(userMapper::toGetUserResponse)
            ?: throw UserNotFoundByUsernameException(username)
    }

    fun findUserById(id: Long): GetUserResponse {
        return userRepository.findById(id)
            .map(userMapper::toGetUserResponse)
            .orElseThrow { UserNotFoundByIdException(id) }
    }

    fun findAllUsers(): GetUsersResponse {
        return userRepository.findAll()
            .map(userMapper::toGetUserResponse)
            .let(::GetUsersResponse)
    }

    fun createUser(createUserDto: CreateUserDto): GetUserResponse {
        val user = userMapper.toUser(createUserDto)
        user.password = bCryptPasswordEncoder.encode(user.password)

        return userRepository.saveAndFlush(user)
            .let(userMapper::toGetUserResponse)
    }

    fun updateUser(updateUserDto: UpdateUserDto): GetUserResponse {
        val user = userMapper.toUser(updateUserDto)

        return userRepository.save(user)
            .let(userMapper::toGetUserResponse)
    }

    fun deleteUser(id: Long): DeleteUserResponse {
        userRepository.deleteById(id)
        return DeleteUserResponse(success = true)
    }
}