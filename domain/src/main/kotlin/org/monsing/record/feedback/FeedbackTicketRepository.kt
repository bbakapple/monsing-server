package org.monsing.record.feedback

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackTicketRepository : JpaRepository<FeedbackTicket, Long> {

    fun findByTeacherId(teacherId: Long): FeedbackTicket?
    fun findByStudentIdAndTeacherId(studentId: Long, teacherId: Long): FeedbackTicket?
}
