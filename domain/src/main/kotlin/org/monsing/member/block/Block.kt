package org.monsing.member.block

import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Block(

    @Id
    val id: String = UUID.randomUUID().toString(),
    val blockerId: Long,
    val blockedId: Long,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
