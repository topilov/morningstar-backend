package me.topilov.morningstar.repository

import me.topilov.morningstar.entity.User
import org.springframework.data.mongodb.repository.DeleteQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    @Query("{ 'username' : ?0 }")
    fun findByUsername(username: String): User?

    fun deleteByUsername(username: String)

    fun existsByUsername(username: String): Boolean
}