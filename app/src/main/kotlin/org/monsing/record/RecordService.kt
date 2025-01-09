package org.monsing.record

import org.monsing.auth.jwt.Role
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.TeacherRepository
import org.monsing.record.feedback.FeedbackTicketRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecordService(
    private val recordRepository: RecordRepository,
    private val feedbackTicketRepository: FeedbackTicketRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository
) {

    @Transactional
    fun saveRecord(record: Record): Record {
        return recordRepository.save(record)
    }

    @Transactional
    fun requestFeedback(memberId: Long, recordId: Long, teacherId: Long) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val student = studentRepository.findByIdOrNull(memberId) ?: throw IllegalArgumentException("Student not found")
        require(record.studentId == student.id) { "Record does not belong to student" }

        val ticket = feedbackTicketRepository.findByStudentIdAndTeacherId(
            requireNotNull(student.id),
            teacherId
        ) ?: throw IllegalArgumentException("Feedback ticket not found")

        ticket.decreaseAmount()
        record.requestFeedback(teacherId)
    }

    @Transactional
    fun writeFeedback(writerId: Long, recordId: Long, detail: String) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        val feedback = record.feedbacks.find { it.teacherId == writerId }
            ?: throw IllegalArgumentException("Feedback not found")

        feedback.writeFeedback(detail)
    }

    @Transactional(readOnly = true)
    fun findRecordsByMemberId(id: Long, role: Role, size: Int?, lastId: Long?): List<Record> {
        return when (role) {
            Role.STUDENT -> recordRepository.findStudentRecordsByMemberIdWithPaging(id, size, lastId)
            Role.TEACHER -> recordRepository.findTeacherRecordsByMemberIdWithPaging(id, size, lastId)
            else -> throw IllegalArgumentException("Role must not be NONE")
        }
    }

    @Transactional(readOnly = true)
    fun findRecordById(recordId: Long, memberId: Long, role: Role): Record {
        require(role != Role.NONE) { "Role must not be NONE" }
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")

        if (role == Role.STUDENT) {
            val student = studentRepository.findByMemberId(memberId)
            require(record.studentId == student?.id) { "Record does not belong to student" }
        }
        if (role == Role.TEACHER) {
            val teacher = teacherRepository.findByMemberId(memberId)
                ?: throw IllegalArgumentException("Teacher not found")
            require(record.containsTeacherFeedback(requireNotNull(teacher.id))) {
                "Record does not contain teacher feedback"
            }
        }

        return record
    }

    @Transactional
    fun deleteRecord(recordId: Long, id: Long) {
        val record = recordRepository.findByIdOrNull(recordId) ?: throw IllegalArgumentException("Record not found")
        require(record.studentId == id) { "Record does not belong to student" }
        record.notCompletedFeedBacks.forEach {
            feedbackTicketRepository.findByStudentIdAndTeacherId(id, it.teacherId)?.increaseAmount()
        }
        recordRepository.delete(record)
    }
}
