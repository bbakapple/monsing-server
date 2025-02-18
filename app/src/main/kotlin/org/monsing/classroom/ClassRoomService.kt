package org.monsing.classroom

import org.monsing.auth.jwt.Role
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.TeacherRepository
import org.springframework.stereotype.Service

@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository,
) {
    fun createClassRoom(teacherId: Long, role: Role, studentId: Int): ClassRoom {
        check(role == Role.TEACHER) { "Only teacher can create a class room" }
        val teacher =
            teacherRepository.findById(teacherId).orElseThrow { throw IllegalArgumentException("Teacher not found") }
        val student = studentRepository.findById(studentId.toLong())
            .orElseThrow { throw IllegalArgumentException("Student not found") }
        return classRoomRepository.save(ClassRoom.create(teacher, student))
    }

    fun retrieveClassRooms(id: Long, role: Role): List<ClassRoom> {
        return when (role) {
            Role.TEACHER -> classRoomRepository.findByTeacherId(id)
            Role.STUDENT -> classRoomRepository.findByStudentId(id)
            Role.NONE -> throw IllegalArgumentException("Role is NONE")
        }
    }
}
