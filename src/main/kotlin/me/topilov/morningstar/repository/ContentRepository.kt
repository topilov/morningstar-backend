package me.topilov.morningstar.repository

import me.topilov.morningstar.entity.Content
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ContentRepository : MongoRepository<Content, String>