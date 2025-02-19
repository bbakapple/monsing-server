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
    val teacherId: Long,

    @Column(nullable = false)
    val duration: Int,

    @Column(nullable = false)
    val pricePerLesson: Int,

    @Column(nullable = false)
    val minimumLessonCount: Int,

    @OneToMany
    val lessons: List<Lesson> = mutableListOf()
) : BaseEntity()
