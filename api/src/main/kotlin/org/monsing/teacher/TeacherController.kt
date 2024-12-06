package org.monsing.teacher

import openapi.api.TeacherApi
import openapi.model.CourseResponse
import openapi.model.ReadReviews200Response
import openapi.model.ReviewCreateRequest
import openapi.model.TeacherCreateRequest
import openapi.model.TeacherDetailResponse
import openapi.model.TeacherResponse
import org.springframework.http.ResponseEntity

class TeacherController: TeacherApi {
    override fun createReview(id: Int, reviewCreateRequest: ReviewCreateRequest): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun createTeacher(teacherCreateRequest: TeacherCreateRequest): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun readClasses(id: Int, lastId: Int?, size: Int?): ResponseEntity<List<CourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun readClassesByDate(id: Int, from: String, to: String): ResponseEntity<List<CourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun readMyInfo(): ResponseEntity<TeacherDetailResponse> {
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
