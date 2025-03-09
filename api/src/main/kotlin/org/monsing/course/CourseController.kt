package org.monsing.course

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import openapi.api.CourseApi
import openapi.model.CourseCreateRequest
import openapi.model.CourseResponse
import openapi.model.CourseUpdateRequest
import openapi.model.DayOfWeekDto
import openapi.model.LessonRegisterRequest
import openapi.model.LessonResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.util.enumValueOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CourseController(
    private val courseService: CourseService
) : CourseApi {

    override fun createCourse(
        tokenPayload: AuthTokenPayload,
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

    private fun DayOfWeekDto.dayOfWeek(): DayOfWeek {
        return enumValueOrNull<DayOfWeek>(name.uppercase())
            ?: throw IllegalArgumentException("Invalid day of week")
    }

    private fun String.time(): LocalTime {
        return LocalTime.parse(this, DateTimeFormatter.ofPattern("HH:mm"))
    }

    override fun updateCourse(
        tokenPayload: AuthTokenPayload,
        courseId: Long,
        courseUpdateRequest: CourseUpdateRequest
    ): ResponseEntity<Unit> {
        courseService.updateCourse(
            tokenPayload.id,
            courseId,
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
        tokenPayload: AuthTokenPayload,
        courseId: Long,
        lessonId: Long,
        lessonRegisterRequest: LessonRegisterRequest
    ): ResponseEntity<Unit> {
        courseService.registerLesson(
            tokenPayload.id,
            courseId,
            lessonId,
            lessonRegisterRequest.lessonCount
        )

        return ResponseEntity.ok().build()
    }

    override fun getCourses(teacherId: Long): ResponseEntity<List<CourseResponse>> {
        val courses = courseService.getCoursesByTeacherId(teacherId)
        return ResponseEntity.ok(courses.map {
            CourseResponse(
                id = requireNotNull(it.id),
                teacherId = it.teacherId,
                name = it.courseOverview.name,
                description = it.courseOverview.description,
                curriculum = it.courseOverview.curriculum,
                duration = it.duration.value,
                price = it.pricePerLesson.value,
                minimumLessonCount = it.minimumLessonCount.value
            )
        })
    }

    override fun getLessons(id: Long): ResponseEntity<List<LessonResponse>> {
        val lessons = courseService.getLessonsByCourseId(id)
        return ResponseEntity.ok(lessons.map {
            LessonResponse(
                id = requireNotNull(it.id),
                dayOfWeek = DayOfWeekDto.valueOf(it.lessonSchedule.dayOfWeek.name),
                startTime = it.lessonSchedule.startTime.toString(),
                isAvailable = it.lessonStatusType.isAvailable()
            )
        })
    }

    override fun getMyLessons(tokenPayload: AuthTokenPayload): ResponseEntity<List<LessonResponse>> {
        val lessons = courseService.getLessonsWithOnAirInfoByMemberId(tokenPayload.id)

        return ResponseEntity.ok(lessons.map {
            LessonResponse(
                id = requireNotNull(it.lesson.id),
                dayOfWeek = DayOfWeekDto.valueOf(it.lesson.lessonSchedule.dayOfWeek.name),
                startTime = it.lesson.lessonSchedule.startTime.toString(),
                isAvailable = it.lesson.lessonStatusType.isAvailable(),
                isOnAir = it.isOnAir
            )
        })
    }
}
