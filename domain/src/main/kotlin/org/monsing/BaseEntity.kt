package org.monsing

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@MappedSuperclass
class BaseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    val updatedDate: LocalDateTime = LocalDateTime.now()
) {
}