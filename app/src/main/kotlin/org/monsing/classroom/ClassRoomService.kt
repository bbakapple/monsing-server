package org.monsing.classroom

import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.course.ClassRoomStatusType
import org.monsing.course.LessonRepository
import org.monsing.member.MemberRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ClassRoomService(
    private val classRoomRepository: ClassRoomRepository,
    private val memberRepository: MemberRepository,
    private val lessonRepository: LessonRepository,
    private val liveKitTokenManager: LiveKitTokenManager,
) {

    @Transactional
    fun createClassRoom(memberId: Long, lessonId: Long): ClassRoom {
        val teacher = memberRepository.findTeacherById(memberId)

        val lesson = lessonRepository.findByTeacherIdAndLessonId(requireNotNull(teacher.id), lessonId)
            ?: throw IllegalArgumentException("lesson not found")

        val classRooms = classRoomRepository.findByLessonId(lessonId)

        check(classRooms.none { it.status == ClassRoomStatusType.OPEN }) {
            "Class room already exists"
        }

        lesson.reduceRemainingCount()

        return classRoomRepository.save(ClassRoom(lessonId))
    }

    @Transactional
    fun completeClassRoom(teacherId: Long, classRoomId: Long) {
        val classRoom = classRoomRepository.findByIdOrElseThrow(classRoomId)

        require(lessonRepository.existsByTeacherIdAndLessonId(teacherId, classRoom.lessonId)) {
            "Lesson not found"
        }

        classRoom.complete()
    }

    @Transactional
    fun enterClassRoom(memberId: Long, classRoomId: Long): String {
        val classRoom = classRoomRepository.findByIdOrElseThrow(classRoomId)
        val lesson = lessonRepository.findByIdOrElseThrow(classRoom.lessonId)
        val isExistByTeacherId = lessonRepository.existsByTeacherIdAndLessonId(memberId, classRoom.lessonId)

        check(isExistByTeacherId || lesson.studentId == memberId) {
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
