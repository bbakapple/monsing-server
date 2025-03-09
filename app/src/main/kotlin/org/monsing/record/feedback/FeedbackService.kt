package org.monsing.record.feedback

import org.monsing.member.MemberRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val feedbackTicketRepository: FeedbackTicketRepository,
    private val memberRepository: MemberRepository,
    private val feedbackItemRepository: FeedbackItemRepository,
) {

    fun createFeedbackItem(teacherId: Long, price: Int, description: String, amount: Int) {
        val teacher = memberRepository.findTeacherById(teacherId)
            ?: throw IllegalArgumentException("Teacher not found")
        val feedbackItem = FeedbackItem(teacher, description, price, amount)
        feedbackItemRepository.save(feedbackItem)
    }

    fun getFeedbackItem(itemId: Long): FeedbackItem {
        return feedbackItemRepository.findByIdOrElseThrow(itemId)
    }

    fun getFeedbackItems(): List<FeedbackItem> {
        return feedbackItemRepository.findAll()
    }

    fun purchaseFeedbackTicket(studentId: Long, amount: Int, itemId: Long) {
        val student = memberRepository.findStudentById(studentId)
            ?: throw IllegalArgumentException("Student not found")
        val feedbackItem = feedbackItemRepository.findByIdOrElseThrow(itemId)
        val ticket = FeedbackTicket(feedbackItem, student, amount)
        feedbackItem.decreaseAmount(amount)
        feedbackTicketRepository.save(ticket)
    }

    fun getFeedbackItemsByMemberId(memberId: Long): List<FeedbackItem> {
        val teacher = memberRepository.findTeacherById(memberId)
        if (teacher != null) {
            return feedbackItemRepository.findByTeacher(teacher)
        }
        val student = memberRepository.findStudentById(memberId) ?: throw IllegalArgumentException("Student not found")
        if (student != null) {
            return feedbackTicketRepository.findByStudent(student).map { it.feedbackItem }
        }
        throw IllegalArgumentException("Member not found")
    }
}
