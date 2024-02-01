package me.topilov.morningstar.controller

import com.fasterxml.jackson.annotation.JsonView
import jakarta.validation.Valid
import me.topilov.morningstar.dto.user.CreateUserDto
import me.topilov.morningstar.dto.user.UpdateUserDto
import me.topilov.morningstar.dto.user.response.DeleteUserResponse
import me.topilov.morningstar.dto.user.response.GetUserResponse
import me.topilov.morningstar.dto.user.response.GetUsersResponse
import me.topilov.morningstar.entity.UserDetailsImpl
import me.topilov.morningstar.exception.auth.NonAuthenticationException
import me.topilov.morningstar.mapper.UserMapper
import me.topilov.morningstar.service.UserService
import me.topilov.morningstar.utils.View
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api")
class UserController(
    @Qualifier("userService") private val userService: UserService,
    private val userMapper: UserMapper,
) {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/users")
    fun createUser(@Valid @RequestBody createUserDto: CreateUserDto): GetUserResponse {
        return userService.createUser(createUserDto)
    }

    @GetMapping("/users/me")
    @JsonView(View.AuthenticatedUser::class)
    fun getMyUser(authentication: Authentication?): GetUserResponse {
        if (authentication == null) {
            throw NonAuthenticationException()
        }
        val userDetails = authentication.principal as UserDetailsImpl
        return userMapper.toGetUserResponse(userDetails.user)
    }

    @GetMapping("/users/username/{username}")
    fun getUser(@PathVariable("username") username: String): GetUserResponse {
        return userService.findUserByUsername(username)
    }

    @GetMapping("/users/{userId}")
    fun getUser(@PathVariable("userId") userId: Long): GetUserResponse {
        return userService.findUserById(userId)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    fun getUsers(): GetUsersResponse {
        return userService.findAllUsers()
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/users")
    fun updateUser(@RequestBody updateUserDto: UpdateUserDto): GetUserResponse {
        return userService.updateUser(updateUserDto)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/users/{id}")
    fun deleteUser(@PathVariable id: Long): DeleteUserResponse {
        return userService.deleteUser(id)
    }
}