package org.monsing.record

import org.monsing.auth.Auth
import org.monsing.auth.AuthPayload
import org.monsing.auth.jwt.TokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
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
}
