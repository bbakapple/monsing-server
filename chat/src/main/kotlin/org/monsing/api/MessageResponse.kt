package org.monsing.api

import java.time.LocalDateTime

data class MessageResponse(
    val id: String,
    val senderId: Long,
    val content: String,
    val createdAt: LocalDateTime
)
