package org.monsing.classroom

import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.auth.jwt.Role
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.TeacherRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service


@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository,
    private val liveKitTokenManager: LiveKitTokenManager,
) {
    fun createClassRoom(teacherId: Long, role: Role, studentId: Long): ClassRoom {
        check(role == Role.TEACHER) { "Only teacher can create a class room" }

        val teacher = teacherRepository.findByIdOrElseThrow(teacherId, "존재하지 않는 선생입니다. : $teacherId")
        val student = studentRepository.findByIdOrElseThrow(studentId, "존재하지 않는 학생입니다. : $studentId")

        return classRoomRepository.save(ClassRoom.create(teacher, student))
    }

    fun retrieveClassRooms(id: Long, role: Role): List<ClassRoom> {
        return when (role) {
            Role.TEACHER -> classRoomRepository.findByTeacherId(id)
            Role.STUDENT -> classRoomRepository.findByStudentId(id)
            Role.NONE -> throw IllegalArgumentException("Role is NONE")
        }
    }

    fun completeClassRoom(teacherId: Long, classRoomId: Long) {
        val classRoom = classRoomRepository.findByIdOrElseThrow(classRoomId)
        check(classRoom.teacher.memberId == teacherId) { "Only teacher can complete a class room" }
        classRoom.complete()
    }

    fun enterClassRoom(memberId: Long, role: Role, classRoomId: Long): String {
        val classRoom = classRoomRepository.findByIdOrElseThrow(classRoomId)
        check(classRoom.student.memberId == memberId || classRoom.teacher.memberId == memberId) {
            "Only student can enter a class room"
        }

        classRoom.enter()
        return getLiveKitToken(memberId, role, classRoomId)
    }

    private fun getLiveKitToken(memberId: Long, role: Role, classRoomId: Long) = when (role) {
        Role.TEACHER -> {
            val member = requireNotNull(teacherRepository.findByMemberId(memberId))
            liveKitTokenManager.generateToken(member.nickname.value, classRoomId.toString(), member.id.toString())
        }

        Role.STUDENT -> {
            val member = requireNotNull(studentRepository.findByMemberId(memberId))
            liveKitTokenManager.generateToken(member.nickname.value, classRoomId.toString(), member.id.toString())
        }

        Role.NONE -> throw IllegalArgumentException("Role is NONE")
    }
}
