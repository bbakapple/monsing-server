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

    fun getFeedbackItemsByTeacherId(teacherId: Long?): List<FeedbackItem> {
        val feedbackItems = feedbackItemRepository.findAll()

        return if (teacherId != null) {
            feedbackItems.filter { it.teacher.id == teacherId }
        } else {
            feedbackItems
        }
    }

    fun purchaseFeedbackTicket(studentId: Long, amount: Int, itemId: Long) {
        val student = memberRepository.findStudentById(studentId)
        val feedbackItem = feedbackItemRepository.findByIdOrElseThrow(itemId)
        val ticket = FeedbackTicket(feedbackItem, student, amount)
        feedbackItem.decreaseAmount(amount)
        feedbackTicketRepository.save(ticket)
    }

    fun getFeedbackItemsByMemberId(memberId: Long): List<FeedbackItem> {
        val member = memberRepository.findByIdOrElseThrow(memberId)

        return if (member is Teacher) {
            feedbackItemRepository.findByTeacher(member)
        } else if (member is Student) {
            feedbackTicketRepository.findByStudent(member).map { it.feedbackItem }
        } else {
            throw IllegalArgumentException("Member not found")
        }
    }


    @Transactional
    fun requestFeedback(memberId: Long, recordId: Long, feedbackTicketId: Long) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val student = memberRepository.findStudentById(memberId)
        val feedbackTicket = feedbackTicketRepository.findByIdOrElseThrow(feedbackTicketId)

        feedbackTicket.decreaseAmount(1)
        record.requestFeedback(feedbackTicket.feedbackItem.teacher)
    }

    fun findFeedbacksByMemberId(id: Long): List<Feedback> {
        val member = memberRepository.findByIdOrElseThrow(id)

        return if (member is Student) {
            feedbackRepository.findByStudentId(id)
        } else if (member is Teacher) {
            feedbackRepository.findByTeacher(member)
        } else {
            throw IllegalArgumentException("Member not found")
        }
    }
}
