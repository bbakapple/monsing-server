package org.monsing.chat

import org.springframework.data.mongodb.core.mapping.Document

@Document
class MessageRead(
    val chatId: String,
    val memberId: Long,
    val messageId: String
)
