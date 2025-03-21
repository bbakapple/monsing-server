package org.monsing.course

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalTime

class LessonTest : StringSpec({

    "레슨 등록" {
        val lesson = Lesson(
            lessonSchedule = LessonSchedule(DayOfWeek.FRI, LocalTime.of(10, 0)),
        )

        lesson.register(1, 1)

        lesson.studentId shouldBe 1
        lesson.lessonRemaining shouldBe 1
        lesson.lessonStatusType shouldBe LessonStatusType.RESERVED
    }

    "이미 판매된 레슨에 등록할 수 없다" {
        val lesson = Lesson(
            lessonSchedule = LessonSchedule(DayOfWeek.FRI, LocalTime.of(10, 0)),
            lessonStatusType = LessonStatusType.RESERVED
        )

        shouldThrow<IllegalStateException> {
            lesson.register(1, 1)
        }
    }
})
