package org.monsing.record

import org.monsing.auth.jwt.Role
import org.monsing.member.StudentRepository
import org.springframework.stereotype.Component

@Component
class StudentRecordHandler(
    private val studentRepository: StudentRepository,
    private val recordRepository: RecordRepository
) : RecordRoleHandler {

    override fun canHandle(role: Role): Boolean {
        return role == Role.STUDENT
    }

    override fun findRecordsByMemberId(memberId: Long, size: Int?, lastId: Long?): List<Record> {
        return recordRepository.findStudentRecordsByMemberIdWithPaging(memberId, size, lastId)
    }

    override fun validateRecordOwnership(memberId: Long, record: Record) {
        val student = studentRepository.findByMemberId(memberId)
            ?: throw IllegalArgumentException("Student not found")
        require(record.studentId == student.id) { "Record does not belong to student" }
    }
}
