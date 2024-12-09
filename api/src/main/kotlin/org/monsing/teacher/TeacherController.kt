package org.monsing.teacher

import openapi.api.TeacherApi
import openapi.model.CourseResponse
import openapi.model.ReadReviews200Response
import openapi.model.ReviewCreateRequest
import openapi.model.TeacherCreateRequest
import openapi.model.TeacherDetailResponse
import openapi.model.TeacherResponse
import org.monsing.auth.jwt.TokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TeacherController : TeacherApi {
    override fun createReview(
        tokenPayload: TokenPayload,
        id: Int,
        reviewCreateRequest: ReviewCreateRequest
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun createTeacher(
        tokenPayload: TokenPayload,
        teacherCreateRequest: TeacherCreateRequest
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun readClasses(
        tokenPayload: TokenPayload,
        id: Int,
        lastId: Int?,
        size: Int?
    ): ResponseEntity<List<CourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun readClassesByDate(
        tokenPayload: TokenPayload,
        id: Int,
        from: String,
        to: String
    ): ResponseEntity<List<CourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun readMyInfo(tokenPayload: TokenPayload): ResponseEntity<TeacherDetailResponse> {
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
