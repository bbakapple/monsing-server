package org.monsing.member

import org.monsing.member.teacher.Teacher
import org.monsing.util.findByIdOrElseThrow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findTeacherById(id: Long): Teacher {
        val member = findByIdOrElseThrow(id)
        return member as? Teacher
            ?: throw IllegalArgumentException("Not a teacher member")
    }

    fun findStudentById(id: Long): Student {
        val member = findByIdOrElseThrow(id)
        return member as? Student
            ?: throw IllegalArgumentException("Not a student member")
    }

    fun findAllTeachers(): List<Teacher> {
        return findAll().filterIsInstance<Teacher>()
    }

    @Query(
        """
            SELECT m FROM Member m
            Join Record r on m.id = r.studentId
            WHERE r.id = :recordId
        """
    )
    fun findStudentByRecordId(recordId: Long): Student
}
