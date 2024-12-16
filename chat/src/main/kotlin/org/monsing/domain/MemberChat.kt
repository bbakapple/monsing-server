package org.monsing.domain

import jakarta.persistence.Id
import java.util.UUID
import org.springframework.data.mongodb.core.mapping.Document

@Document
class MemberChat(

    @Id
    val id: String = UUID.randomUUID().toString(),
    val memberId: Long,
    val chatId: String
)
