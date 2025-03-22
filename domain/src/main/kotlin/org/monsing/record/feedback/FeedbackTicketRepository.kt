package org.monsing.record.feedback

import org.monsing.member.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FeedbackTicketRepository : JpaRepository<FeedbackTicket, Long> {

    fun findByStudent(student: Student): List<FeedbackTicket>
    
    @Query("SELECT ft.feedbackItem.id AS itemId, SUM(ft._amount) AS remainingAmount " +
           "FROM FeedbackTicket ft " +
           "WHERE ft.student.id = :studentId " +
           "AND ft.feedbackItem.id IN :itemIds " +
           "GROUP BY ft.feedbackItem.id")
    fun findRemainingTicketCountsByStudentIdAndItemIds(
        @Param("studentId") studentId: Long,
        @Param("itemIds") itemIds: List<Long>
    ): List<RemainingTicketCount>
    
    fun findByStudentAndFeedbackItem(student: Student, feedbackItem: FeedbackItem): FeedbackTicket?
}

interface RemainingTicketCount {
    fun getItemId(): Long
    fun getRemainingAmount(): Int
}
