package org.monsing.classroom

import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.MemberRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service


@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val memberRepository: MemberRepository,
    private val liveKitTokenManager: LiveKitTokenManager,
) {
    fun createClassRoom(memberId: Long, studentId: Long): ClassRoom {
        val teacher = memberRepository.findTeacherByMemberId(memberId)
            ?: throw IllegalArgumentException("Teacher not found")

        val student = memberRepository.findStudentByMemberId(studentId)
            ?: throw IllegalArgumentException("Student not found")

        return classRoomRepository.save(ClassRoom.create(teacher, student))
    }

    fun retrieveClassRooms(id: Long): List<ClassRoom> {
        return classRoomRepository.findByStudentIdOrTeacherId(id, id)
    }

    fun completeClassRoom(teacherId: Long, classRoomId: Long) {
        val classRoom = classRoomRepository.findByIdOrElseThrow(classRoomId)
        check(classRoom.teacher.id == teacherId) { "Only teacher can complete a class room" }
        classRoom.complete()
    }

    fun enterClassRoom(memberId: Long, classRoomId: Long): String {
        val classRoom = classRoomRepository.findByIdOrElseThrow(classRoomId)
        check(classRoom.student.id == memberId || classRoom.teacher.id == memberId) {
            "Only student can enter a class room"
        }

        classRoom.enter()
        return getLiveKitToken(memberId, classRoomId)
    }

    private fun getLiveKitToken(memberId: Long, classRoomId: Long): String {
        val member = memberRepository.findByIdOrElseThrow(memberId)
        return liveKitTokenManager.generateToken(member.nickname.value, classRoomId.toString(), member.id.toString())
    }
}
