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
    var lessonStatusType: LessonStatusType = LessonStatusType.AVAILABLE
) : BaseEntity(id = id) {

    fun register(id: Long, lessonCount: Int) {
        require(lessonStatusType == LessonStatusType.AVAILABLE) {
            "Lesson is not available"
        }

        studentId = id
        lessonRemaining = lessonCount
        lessonStatusType = LessonStatusType.RESERVED
    }

    fun overlappingWith(lesson: Lesson, duration: Int) {
        if (lessonSchedule.dayOfWeek == lesson.lessonSchedule.dayOfWeek &&
            lessonSchedule.startTime > lesson.lessonSchedule.startTime &&
            lessonSchedule.startTime < lesson.lessonSchedule.startTime.plusMinutes(duration.toLong())
        ) {
            lessonStatusType = LessonStatusType.NOT_AVAILABLE
        }
    }

    fun reduceRemainingCount() {
        check(lessonStatusType == LessonStatusType.RESERVED) {
            "Lesson is not reserved"
        }
        check(requireNotNull(lessonRemaining) > 0) {
            "Lesson count is lesser than 0"
        }
    }
}
