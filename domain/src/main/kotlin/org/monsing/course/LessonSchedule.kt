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
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LessonSchedule) return false

        if (dayOfWeek != other.dayOfWeek) return false
        if (startTime != other.startTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dayOfWeek.hashCode()
        result = 31 * result + startTime.hashCode()
        return result
    }
}
