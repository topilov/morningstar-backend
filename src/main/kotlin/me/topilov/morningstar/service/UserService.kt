package me.topilov.morningstar.service

import me.topilov.morningstar.entity.User
import me.topilov.morningstar.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getUser(id: Int): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun insertUser(user: User): User {
        return userRepository.insert(user)
    }

    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUser(id: Int) {
        userRepository.deleteById(id)
    }
}