package org.monsing.course

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalTime

@Embeddable
class LessonSchedule(

    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek,

    @Column(nullable = false)
    val startTime: LocalTime,
)
