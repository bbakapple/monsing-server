package org.monsing.classroom

import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.course.ClassRoomStatusType
import org.monsing.course.LessonRepository
import org.monsing.member.MemberRepository
import org.monsing.member.Student
import org.monsing.member.teacher.Teacher
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ClassRoomService(
    private val memberRepository: MemberRepository,
    private val lessonRepository: LessonRepository,
    private val liveKitTokenManager: LiveKitTokenManager,
) {

    @Transactional
    fun completeClassRoom(teacherId: Long, lessonId: Long) {
        val lesson = lessonRepository.findByIdOrElseThrow(lessonId)
        val teacher = memberRepository.findTeacherById(teacherId)
        require(lessonRepository.existsByTeacherIdAndLessonId(teacherId, lessonId)) {
            "lesson is not matched"
        }

        lesson.completeClassRoom()
    }

    @Transactional
    fun enterClassRoom(memberId: Long, lessonId: Long): String {
        val lesson = lessonRepository.findByIdOrElseThrow(lessonId)
        val member = memberRepository.findByIdOrElseThrow(memberId)

        if (member is Teacher) {
            require(lessonRepository.existsByTeacherIdAndLessonId(memberId, lessonId)) {
                "Teacher is not matched"
            }
            lesson.openClassRoom()
        }

        if (member is Student) {
            require(lesson.studentId == memberId) {
                "Student is not matched"
            }
            check(lesson.classRoomStatusType == ClassRoomStatusType.OPEN) {
                "ClassRoom is not opened"
            }
            check(lesson.existsRemainingLessonCount) {
                "Lesson is not available"
            }
        }

        return getLiveKitToken(memberId, lessonId)
    }

    private fun getLiveKitToken(memberId: Long, lessonId: Long): String {
        val member = memberRepository.findByIdOrElseThrow(memberId)
        return liveKitTokenManager.generateToken(member.nickname.value, lessonId.toString(), member.id.toString())
    }
}
