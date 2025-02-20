package org.monsing.course

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class Lesson(

    @Embedded
    val lessonSchedule: LessonSchedule,

    var studentId: Long? = null,

    var lessonRemaining: Int? = null,

    @Column(nullable = false)
    var isSold: Boolean = false
) : BaseEntity() {

    fun register(id: Long, lessonCount: Int) {
        require(isSold.not()) {
            "Lesson is already sold"
        }

        studentId = id
        lessonRemaining = lessonCount
        isSold = true
    }
}
