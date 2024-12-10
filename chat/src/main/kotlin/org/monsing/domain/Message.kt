package org.monsing.domain

class Message(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val content: String
) {
}
