package org.monsing.record.feedback

import org.monsing.member.MemberRepository
import org.monsing.member.Student
import org.monsing.member.teacher.Teacher
import org.monsing.record.RecordRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val feedbackTicketRepository: FeedbackTicketRepository,
    private val memberRepository: MemberRepository,
    private val feedbackItemRepository: FeedbackItemRepository,
    private val recordRepository: RecordRepository
) {

    fun createFeedbackItem(teacherId: Long, price: Int, description: String, amount: Int) {
        val teacher = memberRepository.findTeacherById(teacherId)
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
        val feedbackItem = feedbackItemRepository.findByIdOrElseThrow(itemId)
        val ticket = FeedbackTicket(feedbackItem, student, amount)
        feedbackItem.decreaseAmount(amount)
        feedbackTicketRepository.save(ticket)
    }

    fun getFeedbackItemsByMemberId(memberId: Long): List<FeedbackItem> {
        val teacher = getTeacherOrNull(memberId)
        if (teacher != null) {
            return feedbackItemRepository.findByTeacher(teacher)
        }
        val student = getStudentOrNull(memberId)
        if (student != null) {
            return feedbackTicketRepository.findByStudent(student).map { it.feedbackItem }
        }
        throw IllegalArgumentException("Member not found")
    }


    @Transactional
    fun requestFeedback(memberId: Long, recordId: Long, feedbackTicketId: Long) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val student = memberRepository.findStudentById(memberId)
        val feedbackTicket = feedbackTicketRepository.findByIdOrElseThrow(feedbackTicketId)

        feedbackTicket.decreaseAmount(1)
        record.requestFeedback(feedbackTicket.feedbackItem.teacher, record)
    }

    fun findFeedbacksByMemberId(id: Long): List<Feedback> {
        val student = getStudentOrNull(id)
        if (student != null) {
            return feedbackRepository.findByStudentId(id)
        }
        val teacher = getTeacherOrNull(id)
        if (teacher != null) {
            return feedbackRepository.findByTeacher(teacher)
        }
        throw IllegalArgumentException("Member not found")
    }

    private fun getStudentOrNull(memberId: Long): Student? {
        return try {
            memberRepository.findStudentById(memberId)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun getTeacherOrNull(memberId: Long): Teacher? {
        return try {
            memberRepository.findTeacherById(memberId)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
