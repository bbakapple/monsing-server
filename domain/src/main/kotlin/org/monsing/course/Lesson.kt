package org.monsing.course

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class Lesson(

    id: Long? = null,

    @Embedded
    val lessonSchedule: LessonSchedule,

    var studentId: Long? = null,

    var lessonRemaining: Int? = null,

    @Column(nullable = false)
    var lessonStatusType: LessonStatusType = LessonStatusType.AVAILABLE,

    @Column(nullable = false)
    var classRoomStatusType: ClassRoomStatusType = ClassRoomStatusType.CLOSED
) : BaseEntity(id = id) {

    val existsRemainingLessonCount
        get() = (lessonRemaining ?: 0) > 0

    fun register(id: Long, lessonCount: Int) {
        check(lessonStatusType.isAvailable()) {
            "Lesson is not available"
        }

        studentId = id
        lessonRemaining = lessonCount
        lessonStatusType = LessonStatusType.RESERVED
    }

    fun overlappingWith(lesson: Lesson, duration: Int) {
        if (lessonSchedule.dayOfWeek == lesson.lessonSchedule.dayOfWeek &&
            lessonSchedule.startTime >= lesson.lessonSchedule.startTime &&
            lessonSchedule.startTime < lesson.lessonSchedule.startTime.plusMinutes(duration.toLong())
        ) {
            if (lessonStatusType.isAvailable()) {
                lessonStatusType = LessonStatusType.NOT_AVAILABLE
            }
        }
    }

    fun openClassRoom() {
        classRoomStatusType = ClassRoomStatusType.OPEN
    }

    fun completeClassRoom() {
        check(classRoomStatusType == ClassRoomStatusType.OPEN) {
            "ClassRoom is not opened"
        }
        classRoomStatusType = ClassRoomStatusType.CLOSED
        reduceRemainingCount()
    }

    private fun reduceRemainingCount() {
        check(lessonStatusType == LessonStatusType.RESERVED) {
            "Lesson is not reserved"
        }
        check(requireNotNull(lessonRemaining) > 0) {
            "Lesson count is lesser than 0"
        }
        lessonRemaining = lessonRemaining?.minus(1)
    }
}
