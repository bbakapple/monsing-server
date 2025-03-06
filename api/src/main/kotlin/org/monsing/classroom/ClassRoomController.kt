package org.monsing.classroom

import openapi.api.ClassRoomApi
import openapi.model.ClassRoomCreateResponse
import openapi.model.ClassRoomEnterResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassRoomController(
    private val classRoomService: ClassRoomService
) : ClassRoomApi {

    override fun createClassRoom(
        tokenPayload: AuthTokenPayload,
        lessonId: Long
    ): ResponseEntity<ClassRoomCreateResponse> {
        val classRoom = classRoomService.createClassRoom(tokenPayload.id, lessonId)

        return ResponseEntity.ok(
            ClassRoomCreateResponse(
                classRoomId = requireNotNull(classRoom.id)
            )
        )
    }

    override fun enterClassRoom(authTokenPayload: AuthTokenPayload, id: Long): ResponseEntity<ClassRoomEnterResponse> {
        val token = classRoomService.enterClassRoom(authTokenPayload.id, id)

        return ResponseEntity.ok(
            ClassRoomEnterResponse(
                token = token
            )
        )
    }

    override fun completeClassRoom(authTokenPayload: AuthTokenPayload, id: Long): ResponseEntity<Unit> {
        classRoomService.completeClassRoom(authTokenPayload.id, id)
        return ResponseEntity.ok().build()
    }
}
