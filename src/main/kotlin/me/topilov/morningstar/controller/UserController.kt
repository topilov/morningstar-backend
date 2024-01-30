package me.topilov.morningstar.controller

import com.fasterxml.jackson.annotation.JsonView
import jakarta.servlet.http.HttpServletRequest
import me.topilov.morningstar.entity.User
import me.topilov.morningstar.service.AuthTokenService
import me.topilov.morningstar.service.UserService
import me.topilov.morningstar.utils.View
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
@RequestMapping("/api")
class UserController(
    @Qualifier("userService") private val userService: UserService,
    private val authTokenService: AuthTokenService,
) {

    @PostMapping("/admin/users")
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userService.insertUser(user))
    }

    @GetMapping("/users/me")
    @JsonView(View.Admin::class)
    fun getMyUser(request: HttpServletRequest): ResponseEntity<User> {
        val accessToken = authTokenService.getAccessToken(request)

        return accessToken
            ?.let(userService::findUserByAccessToken)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/users/{username}")
    fun getUser(@PathVariable username: String): ResponseEntity<User> {
        return userService.findUserByUsername(username)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.findAll())
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/users")
    fun saveUser(@RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userService.saveUser(user))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/users/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }
}