package org.monsing.classroom

import openapi.api.ClassRoomApi
import openapi.model.ClassRoomCreateRequest
import openapi.model.ClassRoomCreateResponse
import openapi.model.ClassRoomEnterResponse
import openapi.model.ClassRoomResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassRoomController(
    private val classRoomService: ClassRoomService
) : ClassRoomApi {

    override fun createClassRoom(
        authTokenPayload: AuthTokenPayload,
        classRoomCreateRequest: ClassRoomCreateRequest
    ): ResponseEntity<ClassRoomCreateResponse> {
        val classRoom = classRoomService.createClassRoom(
            authTokenPayload.id,
            authTokenPayload.role,
            classRoomCreateRequest.studentId
        )

        return ResponseEntity.ok(
            ClassRoomCreateResponse(
                classRoomId = requireNotNull(classRoom.id).toInt()
            )
        )
    }

    override fun enterClassRoom(authTokenPayload: AuthTokenPayload, id: Int): ResponseEntity<ClassRoomEnterResponse> {
        val token = classRoomService.enterClassRoom(authTokenPayload.id, authTokenPayload.role, id)

        return ResponseEntity.ok(
            ClassRoomEnterResponse(
                token = token
            )
        )
    }

    override fun retrieveClassRooms(authTokenPayload: AuthTokenPayload): ResponseEntity<List<ClassRoomResponse>> {
        val classRooms = classRoomService.retrieveClassRooms(authTokenPayload.id, authTokenPayload.role)

        return ResponseEntity.ok(
            classRooms.map {
                ClassRoomResponse(
                    classRoomId = requireNotNull(it.id).toInt(),
                    studentId = requireNotNull(it.student.id).toInt(),
                    status = it.status.name
                )
            }
        )
    }

    override fun completeClassRoom(authTokenPayload: AuthTokenPayload, id: Int): ResponseEntity<Unit> {
        classRoomService.completeClassRoom(authTokenPayload.id, id)
        return ResponseEntity.ok().build()
    }
}
