package org.monsing.domain

class Message(
    var id: Long? = null,
    val chatId: String,
    val senderId: Long,
    val content: String
)
