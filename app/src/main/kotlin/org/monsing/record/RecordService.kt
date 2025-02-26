package org.monsing.record

import org.monsing.member.MemberRepository
import org.monsing.record.feedback.Feedback
import org.monsing.record.feedback.FeedbackRepository
import org.monsing.record.feedback.FeedbackTicketRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecordService(
    private val recordRepository: RecordRepository,
    private val feedbackRepository: FeedbackRepository,
    private val feedbackTicketRepository: FeedbackTicketRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun saveRecord(record: Record): Record {
        return recordRepository.save(record)
    }

    @Transactional
    fun requestFeedback(memberId: Long, recordId: Long, teacherId: Long) {
        val record = recordRepository.findByIdOrElseThrow(recordId)
        val student = memberRepository.findStudentById(memberId)

        val ticket = feedbackTicketRepository.findByStudentIdAndTeacherId(
            requireNotNull(student.id),
            teacherId
        ) ?: throw IllegalArgumentException("Feedback ticket not found")

        ticket.decreaseAmount()
        record.requestFeedback(teacherId)
    }

    @Transactional
    fun writeFeedback(writerId: Long, recordId: Long, detail: String) {
        val record = recordRepository.findByIdOrElseThrow(recordId)
        val feedback = record.feedbacks.find { it.teacherId == writerId }
            ?: throw IllegalArgumentException("Feedback not found")

        feedback.writeFeedback(detail)
    }

    @Transactional(readOnly = true)
    fun findRecordsByMemberId(id: Long, size: Int?, lastId: Long?): List<Record> {
        return recordRepository.findRecordsByMemberIdWithPaging(id, size, lastId)
    }

    @Transactional(readOnly = true)
    fun findRecordById(recordId: Long, memberId: Long): Record {
        val record = recordRepository.findByIdOrElseThrow(recordId)
        val member = memberRepository.findByIdOrElseThrow(memberId)

        require(record.isOwnedBy(member)) { "Record does not belong to member" }

        return record
    }

    @Transactional
    fun deleteRecord(recordId: Long, id: Long) {
        val record = recordRepository.findByIdOrElseThrow(recordId)
        val student = memberRepository.findStudentById(id)
        require(record.studentId == student.id) { "Record does not belong to student" }
        record.notCompletedFeedBacks.forEach {
            feedbackTicketRepository.findByStudentIdAndTeacherId(id, it.teacherId)?.increaseAmount()
        }
        recordRepository.delete(record)
    }

    @Transactional
    fun updateRecord(recordId: Long, id: Long, title: String) {
        val record = recordRepository.findByIdOrElseThrow(recordId)
        val student = memberRepository.findStudentById(id)
        require(record.studentId == student.id) { "Record does not belong to student" }
        record.updateTitle(title)
    }

    fun findFeedbacksByTeacherId(id: Long): List<Feedback> {
        return feedbackRepository.findByTeacherId(id)
    }
}
