package org.monsing.record.feedback

import org.monsing.member.teacher.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findByTeacher(id: Teacher): List<Feedback>
    fun findByRecordId(id: Long): List<Feedback>

    @Query("SELECT f FROM Feedback f JOIN FETCH f.teacher")
    fun findAllFeedbackDetails(): List<Feedback>
}
