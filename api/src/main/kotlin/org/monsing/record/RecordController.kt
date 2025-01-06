package org.monsing.record

import org.monsing.auth.Auth
import org.monsing.auth.AuthPayload
import org.monsing.auth.jwt.TokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class RecordController(
    private val recordUploader: RecordUploader,
    private val recordService: RecordService
) {

    @Auth
    @PostMapping("/records")
    fun uploadRecord(
        @RequestPart file: MultipartFile,
        @AuthPayload tokenPayload: TokenPayload
    ): ResponseEntity<RecordUploadResponse> {
        val key = recordUploader.uploadRecord(file)
        recordService.saveRecord(Record(tokenPayload.id, key))

        return ResponseEntity.ok(RecordUploadResponse(key))
    }

    @Auth
    @PostMapping("/records/{recordId}/feedback")
    fun requestFeedback(
        @AuthPayload tokenPayload: TokenPayload,
        @PathVariable recordId: Long,
        @RequestBody request: RequestFeedbackRequest
    ): ResponseEntity<Unit> {
        recordService.requestFeedback(tokenPayload.id, recordId, request.teacherId)
        return ResponseEntity.ok().build()
    }

    @Auth
    @PatchMapping("/records/{recordId}/feedback")
    fun writeFeedback(
        @AuthPayload tokenPayload: TokenPayload,
        @PathVariable recordId: Long,
        @RequestBody request: WriteFeedbackRequest
    ): ResponseEntity<Unit> {
        recordService.writeFeedback(tokenPayload.id, recordId, request.detail)
        return ResponseEntity.ok().build()
    }
}
