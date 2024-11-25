package org.monsing.course

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import java.time.LocalTime

@Entity
class CourseSchedule(
    @ManyToOne
    @JoinColumn(name = "course_id")
    val course: Course,

    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek,

    val startTime: LocalTime,

    val isSold: Boolean
) : BaseEntity()
