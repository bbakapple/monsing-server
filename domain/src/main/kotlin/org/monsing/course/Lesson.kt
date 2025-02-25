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
    var isAvailable: Boolean = true
) : BaseEntity(id = id) {

    fun register(id: Long, lessonCount: Int) {
        require(isAvailable) {
            "Lesson is not available"
        }

        studentId = id
        lessonRemaining = lessonCount
        isAvailable = false
    }

    fun overlappingWith(lesson: Lesson, duration: Int) {
        if (lessonSchedule.dayOfWeek == lesson.lessonSchedule.dayOfWeek &&
            lessonSchedule.startTime > lesson.lessonSchedule.startTime &&
            lessonSchedule.startTime < lesson.lessonSchedule.startTime.plusMinutes(duration.toLong())
        ) {
            isAvailable = false
        }
    }
}
