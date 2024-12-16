package org.monsing.domain

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Message(

    @Id
    var id: String? = null,
    val chatId: String,
    val senderId: Long,
    val content: String
)
