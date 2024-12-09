package org.monsing.student

import openapi.api.StudentApi
import openapi.model.CourseResponse
import openapi.model.StudentCreateRequest
import openapi.model.StudentResponse
import openapi.model.StudentUpdateRequest
import openapi.model.TeacherResponse
import org.monsing.auth.jwt.TokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StudentController : StudentApi {
    override fun createStudent(
        tokenPayload: TokenPayload,
        studentCreateRequest: StudentCreateRequest
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun studentsIdCoursesGet(tokenPayload: TokenPayload, id: Int): ResponseEntity<List<CourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun studentsIdFavoritesGet(tokenPayload: TokenPayload, id: Int): ResponseEntity<List<TeacherResponse>> {
        TODO("Not yet implemented")
    }

    override fun studentsIdFavoritesPost(tokenPayload: TokenPayload, id: Int, body: Int): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun studentsIdNextClassGet(tokenPayload: TokenPayload, id: Int): ResponseEntity<CourseResponse> {
        TODO("Not yet implemented")
    }

    override fun studentsMyGet(tokenPayload: TokenPayload): ResponseEntity<StudentResponse> {
        TODO("Not yet implemented")
    }

    override fun studentsMyPatch(
        tokenPayload: TokenPayload,
        studentUpdateRequest: StudentUpdateRequest
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }
}
