package org.monsing.course

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalTime

class CourseTest : StringSpec({

    "course 정상 생성" {
        val courseOverview = CourseOverview(
            name = "코틀린 기초",
            description = "코틀린 기초 문법을 배웁니다.",
            curriculum = "코틀린 기초 문법을 배웁니다.",
        )

        shouldNotThrowAny {
            Course(
                courseOverview = courseOverview,
                teacherId = 1,
                duration = CourseDuration(1),
                pricePerLesson = CoursePricePerLesson(10000),
                minimumLessonCount = CourseMinimumLessonCount(10),
            )
        }
    }

    "course 생성 시 minimumLessonCount가 0 이하일 경우 예외 발생" {
        val courseOverview = CourseOverview(
            name = "코틀린 기초",
            description = "코틀린 기초 문법을 배웁니다.",
            curriculum = "코틀린 기초 문법을 배웁니다.",
        )

        shouldThrow<IllegalArgumentException> {
            Course(
                courseOverview = courseOverview,
                teacherId = 1,
                duration = CourseDuration(1),
                pricePerLesson = CoursePricePerLesson(10000),
                minimumLessonCount = CourseMinimumLessonCount(0),
            )
        }
    }

    "course 생성 시 pricePerLesson이 0 이하일 경우 예외 발생" {
        val courseOverview = CourseOverview(
            name = "코틀린 기초",
            description = "코틀린 기초 문법을 배웁니다.",
            curriculum = "코틀린 기초 문법을 배웁니다.",
        )

        shouldThrow<IllegalArgumentException> {
            Course(
                courseOverview = courseOverview,
                teacherId = 1,
                duration = CourseDuration(1),
                pricePerLesson = CoursePricePerLesson(0),
                minimumLessonCount = CourseMinimumLessonCount(10),
            )
        }
    }

    "course 생성 시 duration이 0 이하일 경우 예외 발생" {
        val courseOverview = CourseOverview(
            name = "코틀린 기초",
            description = "코틀린 기초 문법을 배웁니다.",
            curriculum = "코틀린 기초 문법을 배웁니다.",
        )

        shouldThrow<IllegalArgumentException> {
            Course(
                courseOverview = courseOverview,
                teacherId = 1,
                duration = CourseDuration(0),
                pricePerLesson = CoursePricePerLesson(10000),
                minimumLessonCount = CourseMinimumLessonCount(10),
            )
        }
    }

    "course 업데이트" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(1),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
        )

        shouldNotThrowAny {
            course.update(
                name = "코틀린 심화",
                description = "코틀린 심화 문법을 배웁니다.",
                curriculum = "코틀린 심화 문법을 배웁니다.",
                duration = 2,
                price = 20000,
                minimumLessonCount = 20,
            )
        }
    }

    "course 업데이트 시 minimumLessonCount가 0 이하일 경우 예외 발생" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(1),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
        )

        shouldThrow<IllegalArgumentException> {
            course.update(
                name = "코틀린 심화",
                description = "코틀린 심화 문법을 배웁니다.",
                curriculum = "코틀린 심화 문법을 배웁니다.",
                duration = 2,
                price = 20000,
                minimumLessonCount = 0,
            )
        }
    }

    "course 업데이트 시 pricePerLesson이 0 이하일 경우 예외 발생" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(1),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
        )

        shouldThrow<IllegalArgumentException> {
            course.update(
                name = "코틀린 심화",
                description = "코틀린 심화 문법을 배웁니다.",
                curriculum = "코틀린 심화 문법을 배웁니다.",
                duration = 2,
                price = 0,
                minimumLessonCount = 20,
            )
        }
    }

    "course 업데이트 시 duration이 0 이하일 경우 예외 발생" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(1),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
        )

        shouldThrow<IllegalArgumentException> {
            course.update(
                name = "코틀린 심화",
                description = "코틀린 심화 문법을 배웁니다.",
                curriculum = "코틀린 심화 문법을 배웁니다.",
                duration = 0,
                price = 20000,
                minimumLessonCount = 20,
            )
        }
    }

    "course 업데이트 시 파라미터가 null일 경우 업데이트되지 않음" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(1),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
        )

        course.update(
            name = null,
            description = null,
            curriculum = null,
            duration = null,
            price = null,
            minimumLessonCount = null,
        )

        course.courseOverview.name shouldBe "코틀린 기초"
        course.courseOverview.description shouldBe "코틀린 기초 문법을 배웁니다."
        course.courseOverview.curriculum shouldBe "코틀린 기초 문법을 배웁니다."
        course.duration.value shouldBe 1
        course.pricePerLesson.value shouldBe 10000
        course.minimumLessonCount.value shouldBe 10
    }

    "최소 시간보다 적은 수업을 등록할 경우 예외 발생" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(1),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
            lessons = listOf(
                Lesson(
                    id = 1,
                    lessonSchedule = LessonSchedule(
                        dayOfWeek = DayOfWeek.MONDAY,
                        startTime = LocalTime.of(10, 0),
                    )
                ),
                Lesson(
                    id = 2,
                    lessonSchedule = LessonSchedule(
                        dayOfWeek = DayOfWeek.MONDAY,
                        startTime = LocalTime.of(12, 0),
                    )
                )
            )
        )

        shouldThrow<IllegalArgumentException> { course.registerLesson(1, 1, 5) }
        shouldNotThrowAny { course.registerLesson(1, 2, 10) }
    }

    "시간이 겹치는 레슨에 등록할 수 없다" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(100),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
            lessons = listOf(
                Lesson(
                    id = 1,
                    lessonSchedule = LessonSchedule(
                        dayOfWeek = DayOfWeek.MONDAY,
                        startTime = LocalTime.of(10, 0),
                    )
                ),
                Lesson(
                    id = 2,
                    lessonSchedule = LessonSchedule(
                        dayOfWeek = DayOfWeek.MONDAY,
                        startTime = LocalTime.of(11, 0),
                    )
                )
            )
        )

        course.registerLesson(1, 1, 10)

        shouldThrow<IllegalArgumentException> { course.registerLesson(1, 2, 10) }
    }

    "시간이 겹치지 않는 레슨에 등록할 수 있다" {
        val course = Course(
            courseOverview = CourseOverview(
                name = "코틀린 기초",
                description = "코틀린 기초 문법을 배웁니다.",
                curriculum = "코틀린 기초 문법을 배웁니다.",
            ),
            teacherId = 1,
            duration = CourseDuration(120),
            pricePerLesson = CoursePricePerLesson(10000),
            minimumLessonCount = CourseMinimumLessonCount(10),
            lessons = listOf(
                Lesson(
                    id = 1,
                    lessonSchedule = LessonSchedule(
                        dayOfWeek = DayOfWeek.MONDAY,
                        startTime = LocalTime.of(10, 0),
                    )
                ),
                Lesson(
                    id = 2,
                    lessonSchedule = LessonSchedule(
                        dayOfWeek = DayOfWeek.MONDAY,
                        startTime = LocalTime.of(12, 0),
                    )
                )
            )
        )

        course.registerLesson(1, 1, 10)

        shouldNotThrowAny { course.registerLesson(1, 2, 10) }
    }
})
