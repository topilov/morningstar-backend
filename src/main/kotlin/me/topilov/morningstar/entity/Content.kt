package me.topilov.morningstar.entity

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import jakarta.persistence.*
import me.topilov.morningstar.utils.View
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "content")
@EntityListeners(AuditingEntityListener::class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
data class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @field:JsonView(View.Internal::class)
    var owner: User? = null,

    var title: String = "",

    var description: String = "",

    var price: Double = 0.0,

    @Column(name = "file_type")
    var fileType: String? = null,

    @Column(name = "file_data")
    @Lob
    var file: ByteArray? = null,

    @Column(name = "image_preview_type")
    var imagePreviewType: String? = null,

    @Column(name = "image_preview_data")
    @Lob
    var imagePreview: ByteArray? = null,

    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
)
