package org.monsing.record.feedback

import org.monsing.member.teacher.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackTicketRepository : JpaRepository<FeedbackTicket, Long> {

    fun findByStudentIdAndTeacher(studentId: Long, teacher: Teacher): FeedbackTicket?
}
