package org.monsing.record

import org.monsing.member.MemberRepository
import org.monsing.record.feedback.Feedback
import org.monsing.record.feedback.FeedbackRepository
import org.monsing.record.feedback.FeedbackTicketRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.data.repository.findByIdOrNull
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
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val student = memberRepository.findStudentById(memberId)
            ?: throw IllegalArgumentException("Student not found")
        val teacher = memberRepository.findTeacherById(teacherId)
            ?: throw IllegalArgumentException("Teacher not found")
        val ticket = feedbackTicketRepository.findByStudentIdAndTeacher(
            requireNotNull(student.id),
            teacher
        ) ?: throw IllegalArgumentException("Feedback ticket not found")

        ticket.decreaseAmount()
        record.requestFeedback(teacher)
    }

    @Transactional
    fun writeFeedback(writerId: Long, recordId: Long, detail: String) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val feedback = record.feedbacks.find { it.teacher.id == writerId }
            ?: throw IllegalArgumentException("Feedback not found")

        feedback.writeFeedback(detail)
    }

    @Transactional(readOnly = true)
    fun findRecordsByMemberId(id: Long, size: Int?, lastId: Long?): List<Record> {
        return recordRepository.findRecordsByMemberIdWithPaging(id, size, lastId)
    }

    @Transactional(readOnly = true)
    fun findRecordById(recordId: Long, memberId: Long): Record {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val member = memberRepository.findByIdOrElseThrow(memberId)

        require(record.isOwnedBy(member)) { "Record does not belong to member" }

        return record
    }

    @Transactional
    fun deleteRecord(recordId: Long, id: Long) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val student = memberRepository.findStudentById(id) ?: throw IllegalArgumentException("Student not found")
        require(record.studentId == student.id) { "Record does not belong to student" }
        record.notCompletedFeedBacks.forEach {
            feedbackTicketRepository.findByStudentIdAndTeacher(id, it.teacher)?.increaseAmount()
        }
        recordRepository.delete(record)
    }

    @Transactional
    fun updateRecord(recordId: Long, id: Long, title: String) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val student = memberRepository.findStudentById(id) ?: throw IllegalArgumentException("Student not found")
        require(record.studentId == student.id) { "Record does not belong to student" }
        record.updateTitle(title)
    }

    fun findAllFeedbackDetails(): List<Feedback> {
        return feedbackRepository.findAllFeedbackDetails()
    }

    fun findFeedbacksByMemberId(id: Long): List<Feedback> {
        val studentFeedbacks = recordRepository.findByStudentId(id).flatMap { it.feedbacks }
        if (studentFeedbacks.isNotEmpty()) return studentFeedbacks

        val teacher = memberRepository.findTeacherById(id) ?: throw IllegalArgumentException("Teacher not found")
        val teacherFeedbacks = feedbackRepository.findByTeacher(teacher)
        if (teacherFeedbacks.isNotEmpty()) return teacherFeedbacks

        return emptyList()
    }
}
