package org.monsing.member

import org.monsing.member.teacher.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findTeacherById(id: Long): Teacher {
        return findByIdOrNull(id) as? Teacher
            ?: throw IllegalArgumentException("Teacher not found")
    }

    fun findStudentById(id: Long): Student {
        return findByIdOrNull(id) as? Student
            ?: throw IllegalArgumentException("Student not found")
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
