package me.topilov.morningstar.service

import me.topilov.morningstar.entity.User
import me.topilov.morningstar.entity.UserDetailsImpl
import me.topilov.morningstar.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) : UserDetailsService  {

    @Autowired
    private lateinit var bCryptPasswordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found")
        return UserDetailsImpl(user)
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun existsById(id: String): Boolean {
        return userRepository.existsById(id)
    }

    fun findUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun findUserById(id: String): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun insertUser(user: User): User {
        user.password = bCryptPasswordEncoder.encode(user.password)
        return userRepository.insert(user)
    }

    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUser(id: String) {
        userRepository.deleteById(id)
    }

    fun deleteUserByUsername(username: String) {
        userRepository.deleteByUsername(username)
    }
}