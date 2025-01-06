package org.monsing.record

import java.time.LocalDateTime

data class FeedbackResponse(
    val id: Long,
    val writerId: Long,
    val detail: String?,
    val createdAt: LocalDateTime?
)

