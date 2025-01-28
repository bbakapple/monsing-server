package org.monsing.record

import org.monsing.auth.jwt.Role
import org.monsing.member.teacher.TeacherRepository
import org.springframework.stereotype.Component

@Component
class TeacherRecordHandler(
    private val teacherRepository: TeacherRepository,
    private val recordRepository: RecordRepository
) : RecordRoleHandler {

    override fun canHandle(role: Role): Boolean {
        return role == Role.TEACHER
    }

    override fun findRecordsByMemberId(memberId: Long, size: Int?, lastId: Long?): List<Record> {
        return recordRepository.findTeacherRecordsByMemberIdWithPaging(memberId, size, lastId)
    }

    override fun validateRecordOwnership(memberId: Long, record: Record) {
        val teacher = teacherRepository.findByMemberId(memberId)
            ?: throw IllegalArgumentException("Teacher not found")
        require(record.containsTeacherFeedback(requireNotNull(teacher.id))) {
            "Record does not contain teacher feedback"
        }
    }
}
