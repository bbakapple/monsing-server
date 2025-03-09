package org.monsing.record.feedback

import org.monsing.member.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackTicketRepository : JpaRepository<FeedbackTicket, Long> {

    fun findByStudent(student: Student): List<FeedbackTicket>

}
