package org.monsing.record.response

import java.time.LocalDateTime
import org.monsing.member.teacher.Teacher

data class FeedbackResponse(
    val id: Long,
    val recordId: Long,
    val teacher: Teacher,
    val student: StudentInfoResponse? = null,
    val detail: String?,
    val createdAt: LocalDateTime?
)

data class StudentInfoResponse(
    val id: Long,
    val name: String,
    val profileImageUrl: String?
)

