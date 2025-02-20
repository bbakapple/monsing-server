package org.monsing.course

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.monsing.BaseEntity

@Entity
class Course(

    @Embedded
    val courseOverview: CourseOverview,

    @Column(nullable = false)
    var teacherId: Long,

    @Embedded
    var duration: CourseDuration,

    @Embedded
    var pricePerLesson: CoursePricePerLesson,

    @Embedded
    var minimumLessonCount: CourseMinimumLessonCount,

    @OneToMany
    val lessons: List<Lesson> = mutableListOf()
) : BaseEntity() {

    fun update(
        name: String?,
        description: String?,
        curriculum: String?,
        duration: Int?,
        price: Int?,
        minimumLessonCount: Int?
    ) {
        courseOverview.update(
            name = name,
            description = description,
            curriculum = curriculum
        )

        price?.let { this.pricePerLesson = CoursePricePerLesson(it) }
        duration?.let { this.duration = CourseDuration(it) }
        minimumLessonCount?.let { this.minimumLessonCount = CourseMinimumLessonCount(it) }
    }
}

class CourseMinimumLessonCount(

    @Column(name = "minimum_lesson_count", nullable = false)
    val value: Int
) {
    init {
        require(value > 0) {
            "Minimum lesson count must be greater than 0"
        }
    }
}

class CoursePricePerLesson(

    @Column(name = "price_per_lesson", nullable = false)
    val value: Int
) {
    init {
        require(value > 0) {
            "Price per lesson must be greater than 0"
        }

    }
}

class CourseDuration(

    @Column(name = "duration", nullable = false)
    val value: Int
) {
    init {
        require(value > 0) {
            "Duration must be greater than 0"
        }
    }
}
