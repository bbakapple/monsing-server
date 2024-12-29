package org.monsing.chat

import jakarta.persistence.Id
import java.time.LocalDateTime
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Message(

    @Id
    var id: String? = null,
    val chatId: String,
    val senderId: Long,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
}
