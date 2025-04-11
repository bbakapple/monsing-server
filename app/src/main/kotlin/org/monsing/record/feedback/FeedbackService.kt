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

    @Transactional
    fun purchaseFeedbackTicket(studentId: Long, amount: Int, itemId: Long) {
        val student = memberRepository.findStudentById(studentId)
        val feedbackItem = feedbackItemRepository.findByIdOrElseThrow(itemId)

        // 학생이 이미 해당 피드백 아이템의 티켓을 가지고 있는지 확인
        val existingTicket = feedbackTicketRepository.findByStudentAndFeedbackItem(student, feedbackItem)

        if (existingTicket != null) {
            // 기존 티켓이 있다면 수량을 증가시킴
            existingTicket.increaseAmount(amount)
        } else {
            // 기존 티켓이 없다면 새로 생성
            val ticket = FeedbackTicket(feedbackItem, student, amount)
            feedbackTicketRepository.save(ticket)
        }

        // 피드백 아이템의 재고 감소
        feedbackItem.decreaseAmount(amount)
    }

    fun getFeedbackItemsByMemberId(memberId: Long): List<FeedbackItem> {
        val member = memberRepository.findByIdOrElseThrow(memberId)

        require(member is Teacher || member is Student) { "Member not found" }

        return if (member is Teacher) {
            feedbackItemRepository.findByTeacher(member)
        } else
            feedbackTicketRepository.findByStudent(member as Student).map { it.feedbackItem }
    }

    fun getRemainingTicketsMapByMemberId(memberId: Long, itemIds: List<Long>): Map<Long, Int> {
        if (itemIds.isEmpty() || !isStudent(memberId)) {
            return emptyMap()
        }

        return getRemainingTicketCountsByItemIds(memberId, itemIds)
    }

    fun isStudent(memberId: Long): Boolean {
        val member = memberRepository.findByIdOrElseThrow(memberId)
        return member is Student
    }

    fun getRemainingTicketCountsByItemIds(studentId: Long, itemIds: List<Long>): Map<Long, Int> {
        val remainingTickets = feedbackTicketRepository.findRemainingTicketCountsByStudentIdAndItemIds(
            studentId,
            itemIds
        )
        return remainingTickets.associate { it.getItemId() to it.getRemainingAmount() }
    }

    @Transactional
    fun requestFeedback(memberId: Long, recordId: Long, feedbackTicketId: Long) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val student = memberRepository.findStudentById(memberId)
        val feedbackTicket = feedbackTicketRepository.findByIdOrElseThrow(feedbackTicketId)

        feedbackTicket.decreaseAmount(1)
        record.requestFeedback(feedbackTicket.feedbackItem.teacher)
    }

    fun findFeedbacksByMemberId(id: Long): List<FeedbackDto> {
        val member = memberRepository.findByIdOrElseThrow(id)

        require(member is Student || member is Teacher) { "Member not found" }

        if (member is Student) {
            return recordRepository.findByStudentId(id).flatMap { record ->
                record.feedbacks.map { FeedbackDto(it, member) }
            }
        } else {
            val feedbacks = feedbackRepository.findByTeacher(member as Teacher)
            return feedbacks.map {
                FeedbackDto(
                    it, memberRepository.findStudentByRecordId(it.recordId)
                )
            }
        }
    }
}

data class FeedbackDto(
    val feedback: Feedback,
    val student: Student
)
