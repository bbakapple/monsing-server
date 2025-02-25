package org.monsing.teacher

import openapi.api.TeacherApi
import openapi.model.PrevCourseResponse
import openapi.model.ReadReviews200Response
import openapi.model.ReviewCreateRequest
import openapi.model.TeacherCreateRequest
import openapi.model.TeacherDetailResponse
import openapi.model.TeacherResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.member.teacher.GenderType
import org.monsing.util.enumValueOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TeacherController(
    private val teacherService: TeacherService
) : TeacherApi {
    override fun createReview(
        authTokenPayload: AuthTokenPayload,
        id: Int,
        reviewCreateRequest: ReviewCreateRequest
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

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

    override fun readClasses(
        tokenPayload: AuthTokenPayload,
        id: Int,
        lastId: Int?,
        size: Int?
    ): ResponseEntity<List<PrevCourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun readClassesByDate(
        tokenPayload: AuthTokenPayload,
        id: Int,
        from: String,
        to: String
    ): ResponseEntity<List<PrevCourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun readMyInfo(authTokenPayload: AuthTokenPayload): ResponseEntity<TeacherDetailResponse> {
        TODO("Not yet implemented")
    }

    override fun readReviews(id: Int, lastId: Int?, size: Int?): ResponseEntity<ReadReviews200Response> {
        TODO("Not yet implemented")
    }

    override fun readTeacherDetail(id: Int): ResponseEntity<TeacherDetailResponse> {
        TODO("Not yet implemented")
    }

    override fun readTeachers(
        lastId: Int?,
        size: Int?,
        gender: String?,
        maxPrice: Int?,
        verified: Boolean?,
        query: String?
    ): ResponseEntity<TeacherResponse> {
        TODO("Not yet implemented")
    }
}
