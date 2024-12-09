package org.monsing.auth.teacher

import org.monsing.member.teacher.GenderType
import org.monsing.member.teacher.Teacher
import org.monsing.member.teacher.TeacherRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeacherService(private val teacherRepository: TeacherRepository) {

    @Transactional
    fun createTeacher(teacher: Teacher) {
        teacherRepository.save(teacher)
    }

    @Transactional(readOnly = true)
    fun findTeachersByConditions(
        genderType: GenderType?,
        verified: Boolean?,
        size: Int?,
        lastId: Long?,
        keyword: String?,
        price: Int?
    ): List<Teacher> {
        return teacherRepository.findByConditions(genderType, verified, size, lastId, keyword, price)
    }

    @Transactional(readOnly = true)
    fun findTeacherById(id: Long): Teacher {
        return teacherRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("Teacher not found")
    }
}
