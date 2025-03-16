package org.monsing.teacher

import openapi.api.TeacherApi
import openapi.model.CareerResponse
import openapi.model.TeacherCreateRequest
import openapi.model.TeacherOverviewResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.member.teacher.GenderType
import org.monsing.util.enumValueOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TeacherController(
    private val teacherService: TeacherService
) : TeacherApi {

    override fun createTeacher(
        authTokenPayload: AuthTokenPayload,
        teacherCreateRequest: TeacherCreateRequest
    ): ResponseEntity<Unit> {
        teacherService.createTeacher(
            authTokenPayload.id,
            teacherCreateRequest.name,
            teacherCreateRequest.gender()
        )

        return ResponseEntity.ok().build()
    }

    private fun TeacherCreateRequest.gender(): GenderType? {
        return enumValueOrNull<GenderType>(
            gender.name.uppercase()
        )
    }

    override fun getTeacherOverview(id: Long): ResponseEntity<TeacherOverviewResponse> {
        val teacher = teacherService.findTeacherById(id)

        val response = TeacherOverviewResponse(
            id = requireNotNull(teacher.id),
            name = teacher.nickname.value,
            verified = teacher.verified,
            careers = teacher.careers.map { CareerResponse(it.detail, it.period) },
            portfolios = teacher.portfolios.map { it.url },
            profileImage = teacher.profileImage,
            summary = teacher.summary,
            description = teacher.description
        )

        return ResponseEntity.ok(response)
    }

    override fun readTeachers(): ResponseEntity<List<TeacherOverviewResponse>> {
        val teachers = teacherService.findAllTeachers()

        return ResponseEntity.ok(
            teachers.map {
                TeacherOverviewResponse(
                    id = it.id!!,
                    name = it.nickname.value,
                    verified = it.verified,
                    careers = it.careers.map { career -> CareerResponse(career.detail, career.period) },
                    portfolios = it.portfolios.map { portfolio -> portfolio.url },
                    profileImage = it.profileImage,
                    summary = it.summary,
                    description = it.description
                )
            }
        )
    }
}
