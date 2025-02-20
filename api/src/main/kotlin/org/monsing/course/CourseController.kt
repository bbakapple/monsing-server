package org.monsing.course

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import openapi.api.CourseApi
import openapi.model.CourseCreateRequest
import openapi.model.CourseUpdateRequest
import openapi.model.DayOfWeekRequest
import openapi.model.LessonRegisterRequest
import org.monsing.auth.jwt.TokenPayload
import org.monsing.util.enumValueOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CourseController(
    private val courseService: CourseService
) : CourseApi {

    override fun createCourse(
        tokenPayload: TokenPayload,
        courseCreateRequest: CourseCreateRequest
    ): ResponseEntity<Unit> {
        val lessonSchedules = courseCreateRequest.lessonSchedule.map {
            LessonSchedule(
                dayOfWeek = it.dayOfWeek.dayOfWeek(),
                startTime = it.startTime.time(),
            )
        }
        courseService.createCourse(
            tokenPayload.id,
            tokenPayload.role,
            courseCreateRequest.name,
            courseCreateRequest.description,
            courseCreateRequest.curriculum,
            courseCreateRequest.duration,
            courseCreateRequest.price,
            courseCreateRequest.minimumLessonCount,
            lessonSchedules
        )

        return ResponseEntity.ok().build()
    }

    private fun DayOfWeekRequest.dayOfWeek(): DayOfWeek {
        return enumValueOrNull<DayOfWeek>(name.uppercase())
            ?: throw IllegalArgumentException("Invalid day of week")
    }

    private fun String.time(): LocalTime {
        return LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))
    }

    override fun updateCourse(
        tokenPayload: TokenPayload,
        id: Long,
        courseUpdateRequest: CourseUpdateRequest
    ): ResponseEntity<Unit> {
        courseService.updateCourse(
            tokenPayload.id,
            id,
            courseUpdateRequest.name,
            courseUpdateRequest.description,
            courseUpdateRequest.curriculum,
            courseUpdateRequest.duration,
            courseUpdateRequest.price,
            courseUpdateRequest.minimumLessonCount
        )
        return ResponseEntity.ok().build()
    }

    override fun registerLesson(
        tokenPayload: TokenPayload,
        courseId: Long,
        lessonId: Long,
        lessonRegisterRequest: LessonRegisterRequest
    ): ResponseEntity<Unit> {
        courseService.registerLesson(
            tokenPayload.id,
            tokenPayload.role,
            courseId,
            lessonId,
            lessonRegisterRequest.lessonCount
        )

        return ResponseEntity.ok().build()
    }
}
