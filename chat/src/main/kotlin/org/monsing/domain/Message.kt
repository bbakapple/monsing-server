package org.monsing.domain

class Message(
    var id: String? = null,
    val chatId: String,
    val senderId: Long,
    val content: String
)
