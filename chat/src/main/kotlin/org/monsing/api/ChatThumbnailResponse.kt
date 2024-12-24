package org.monsing.api

import java.time.LocalDateTime

data class ChatThumbnailResponse(
    val id: String,
    val senderId: Long?,
    val lastMessage: String?,
    val lastMessageTime: LocalDateTime?
)
