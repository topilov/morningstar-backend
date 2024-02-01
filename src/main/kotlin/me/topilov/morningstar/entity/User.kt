package me.topilov.morningstar.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var username: String = "",
    var password: String = "",
    var role: String = "ROLE_USER",
    var balance: Double = 0.0,
    var isLocked: Boolean = false,

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    val contents: MutableList<Content> = mutableListOf(),

    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
)
