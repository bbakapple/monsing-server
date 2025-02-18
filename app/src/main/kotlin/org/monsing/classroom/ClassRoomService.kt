package org.monsing.classroom

import org.monsing.auth.jwt.Role
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.TeacherRepository
import org.monsing.token.Token
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

    fun completeClassRoom(teacherId: Long, classRoomId: Int) {
        val classRoom = classRoomRepository.findById(classRoomId.toLong())
            .orElseThrow { throw IllegalArgumentException("Class room not found") }
        check(classRoom.teacher.memberId == teacherId) { "Only teacher can complete a class room" }
        classRoom.complete()
    }

    fun enterClassRoom(memberId: Long, classRoomId: Int): Token {
        val classRoom = classRoomRepository.findById(classRoomId.toLong())
            .orElseThrow { throw IllegalArgumentException("Class room not found") }
        check(classRoom.student.memberId == memberId || classRoom.teacher.memberId == memberId) {
            "Only student can enter a class room"
        }
        classRoom.enter()
        return Token("", "")
    }
}
