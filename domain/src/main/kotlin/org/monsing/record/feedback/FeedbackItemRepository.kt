package org.monsing.record.feedback

import org.monsing.member.teacher.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackItemRepository : JpaRepository<FeedbackItem, Long> {
    fun findByTeacher(teacher: Teacher): MutableList<FeedbackItem>
}
