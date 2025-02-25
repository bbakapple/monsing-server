package org.monsing.student

import openapi.api.StudentApi
import openapi.model.CourseResponse
import openapi.model.StudentCreateRequest
import openapi.model.StudentResponse
import openapi.model.StudentUpdateRequest
import openapi.model.TeacherResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class StudentController(
    private val studentService: StudentService
) : StudentApi {

    override fun createStudent(
        authTokenPayload: AuthTokenPayload,
        studentCreateRequest: StudentCreateRequest
    ): ResponseEntity<Unit> {
        studentService.create(authTokenPayload.id, studentCreateRequest.name)

        return ResponseEntity.ok().build()
    }

    override fun studentsIdCoursesGet(
        authTokenPayload: AuthTokenPayload,
        id: Int
    ): ResponseEntity<List<CourseResponse>> {
        TODO("Not yet implemented")
    }

    override fun studentsIdFavoritesGet(
        authTokenPayload: AuthTokenPayload,
        id: Int
    ): ResponseEntity<List<TeacherResponse>> {
        TODO("Not yet implemented")
    }

    override fun studentsIdFavoritesPost(authTokenPayload: AuthTokenPayload, id: Int, body: Int): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun studentsIdNextClassGet(authTokenPayload: AuthTokenPayload, id: Int): ResponseEntity<CourseResponse> {
        TODO("Not yet implemented")
    }

    override fun studentsMyGet(authTokenPayload: AuthTokenPayload): ResponseEntity<StudentResponse> {
        TODO("Not yet implemented")
    }

    override fun studentsMyPatch(
        authTokenPayload: AuthTokenPayload,
        studentUpdateRequest: StudentUpdateRequest
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }
}
