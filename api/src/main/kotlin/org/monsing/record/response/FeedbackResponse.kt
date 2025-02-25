package org.monsing.record.response

import java.time.LocalDateTime

data class FeedbackResponse(
    val id: Long,
    val recordId: Long,
    val writerId: Long,
    val detail: String?,
    val createdAt: LocalDateTime?
)

