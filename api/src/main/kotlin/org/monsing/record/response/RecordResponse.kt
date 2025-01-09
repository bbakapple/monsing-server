package org.monsing.record.response

import java.time.LocalDateTime

data class RecordResponse(
    val id: Long,
    val url: String,
    val createdAt: LocalDateTime,
    val feedbacks: List<FeedbackResponse> = emptyList()
)
