package org.monsing.record.response

import org.monsing.member.teacher.Teacher
import java.time.LocalDateTime

data class FeedbackResponse(
    val id: Long,
    val recordId: Long,
    val teacher: Teacher,
    val detail: String?,
    val createdAt: LocalDateTime?
)

