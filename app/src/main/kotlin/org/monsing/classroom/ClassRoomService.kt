package org.monsing.classroom

import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.MemberRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val memberRepository: MemberRepository,
    private val liveKitTokenManager: LiveKitTokenManager,
) {

    @Transactional
    fun createClassRoom(memberId: Long, studentId: Long): ClassRoom {
        val teacher = memberRepository.findTeacherById(memberId)
            ?: throw IllegalArgumentException("Teacher not found")

        val student = memberRepository.findStudentById(studentId)
            ?: throw IllegalArgumentException("Student not found")

        return classRoomRepository.save(ClassRoom.create(teacher, student))
    }

    @Transactional
    fun retrieveClassRooms(id: Long): List<ClassRoom> {
        return classRoomRepository.findByStudentIdOrTeacherId(id, id)
    }

    @Transactional
    fun completeClassRoom(teacherId: Long, classRoomId: Long) {
        val classRoom = classRoomRepository.findByIdOrElseThrow(classRoomId)
        check(classRoom.teacher.id == teacherId) { "Only teacher can complete a class room" }
        classRoom.complete()
    }

    @Transactional
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
