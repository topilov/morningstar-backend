package me.topilov.morningstar.controller

import me.topilov.morningstar.entity.User
import me.topilov.morningstar.service.JwtService
import me.topilov.morningstar.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class UserController(
    @Qualifier("userService") private val userService: UserService,
    private val jwtService: JwtService,
) {
    @PostMapping("/users")
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userService.insertUser(user))
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<User> {
        return userService.findUserById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.findAll())
    }

    @PutMapping("/users")
    fun saveUser(@RequestBody user: User): ResponseEntity<User> {
        return ResponseEntity.ok(userService.saveUser(user))
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/role")
    fun getRole() = SecurityContextHolder.getContext().authentication.authorities.first().toString()
}