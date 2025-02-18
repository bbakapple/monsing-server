package org.monsing.classroom

import openapi.api.ClassRoomApi
import openapi.model.ClassRoomCreateRequest
import openapi.model.ClassRoomCreateResponse
import openapi.model.ClassRoomResponse
import org.monsing.auth.jwt.TokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ClassRoomController(
    private val classRoomService: ClassRoomService
) : ClassRoomApi {

    override fun createClassRoom(
        tokenPayload: TokenPayload,
        classRoomCreateRequest: ClassRoomCreateRequest
    ): ResponseEntity<ClassRoomCreateResponse> {
        val classRoom = classRoomService.createClassRoom(
            tokenPayload.id,
            tokenPayload.role,
            classRoomCreateRequest.studentId
        )

        return ResponseEntity.ok(
            ClassRoomCreateResponse(
                classRoomId = requireNotNull(classRoom.id).toInt()
            )
        )
    }

    override fun retrieveClassRooms(tokenPayload: TokenPayload): ResponseEntity<List<ClassRoomResponse>> {
        val classRooms = classRoomService.retrieveClassRooms(tokenPayload.id, tokenPayload.role)

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

    override fun completeClassRoom(tokenPayload: TokenPayload, id: Int): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun enterClassRoom(tokenPayload: TokenPayload, id: Int): ResponseEntity<ClassRoomResponse> {
        TODO("Not yet implemented")
    }
}
