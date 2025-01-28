package org.monsing.student

import org.monsing.member.Student
import org.monsing.member.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(
    private val studentRepository: StudentRepository
) {

    @Transactional
    fun create(student: Student) {
        studentRepository.save(student)
    }
}
