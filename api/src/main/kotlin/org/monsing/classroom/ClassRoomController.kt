package org.monsing.classroom

import openapi.api.ClassRoomApi
import openapi.model.ClassRoomEnterResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassRoomController(
    private val classRoomService: ClassRoomService
) : ClassRoomApi {

    override fun enterClassRoom(
        authTokenPayload: AuthTokenPayload,
        lessonId: Long
    ): ResponseEntity<ClassRoomEnterResponse> {
        val token = classRoomService.enterClassRoom(authTokenPayload.id, lessonId)

        return ResponseEntity.ok(
            ClassRoomEnterResponse(
                token = token
            )
        )
    }

    override fun completeClassRoom(authTokenPayload: AuthTokenPayload, lessonId: Long): ResponseEntity<Unit> {
        classRoomService.completeClassRoom(authTokenPayload.id, lessonId)
        return ResponseEntity.ok().build()
    }
}
