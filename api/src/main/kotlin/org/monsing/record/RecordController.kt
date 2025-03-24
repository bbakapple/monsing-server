package org.monsing.record

import io.swagger.v3.oas.annotations.Operation
import org.monsing.auth.Auth
import org.monsing.auth.AuthPayload
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.record.feedback.FeedbackTicket
import org.monsing.record.request.UpdateRecordRequest
import org.monsing.record.request.UploadRecordRequest
import org.monsing.record.request.WriteFeedbackRequest
import org.monsing.record.response.FeedbackResponse
import org.monsing.record.response.RecordResponse
import org.monsing.record.response.RecordUploadResponse
import org.monsing.util.toNonNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class RecordController(
    private val recordUploader: RecordUploader,
    private val recordService: RecordService,
    @Value("\${aws.cloudfront-url}") private val cloudfrontUrl: String
) {

    @Auth
    @Operation(summary = "Record 파일 업로드")
    @PostMapping("/records")
    fun uploadRecord(
        @RequestPart(value = "file") file: MultipartFile,
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @RequestPart(value = "request") request: UploadRecordRequest
    ): ResponseEntity<RecordUploadResponse> {
        val key = recordUploader.uploadRecord(file)
        val record = recordService.saveRecord(
            Record(
                title = request.title,
                studentId = authTokenPayload.id,
                fileKey = key.key,
                url = key.url
            )
        )

        return ResponseEntity.ok(RecordUploadResponse(record.id.toNonNull()))
    }
//
//    @Auth
//    @Operation(summary = "feedback 요청")
//    @PostMapping("/records/{recordId}/feedbacks")
//    fun requestFeedback(
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @PathVariable recordId: Long,
//        @RequestBody request: RequestFeedbackRequest
//    ): ResponseEntity<Unit> {
//        recordService.requestFeedback(authTokenPayload.id, recordId, request.teacherId)
//        return ResponseEntity.ok().build()
//    }

    @Auth
    @Operation(summary = "feedback 작성")
    @PatchMapping("/records/{recordId}/feedbacks")
    fun writeFeedback(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @PathVariable recordId: Long,
        @RequestBody request: WriteFeedbackRequest
    ): ResponseEntity<Unit> {
        recordService.writeFeedback(authTokenPayload.id, recordId, request.detail)
        return ResponseEntity.ok().build()
    }

    @Auth
    @Operation(summary = "내 record 조회")
    @GetMapping("/records/my")
    fun listRecords(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @RequestParam(required = false) size: Int?,
        @RequestParam(required = false) lastId: Long?
    ): ResponseEntity<List<RecordResponse>> {
        val records = recordService.findRecordsByMemberId(authTokenPayload.id, size, lastId)

        val response = records.map {
            RecordResponse(
                id = requireNotNull(it.id),
                url = it.fileKey.toUrl(),
                createdAt = it.createdDate
            )
        }

        return ResponseEntity.ok(response)
    }

    @Auth
    @Operation(summary = "record 단건 조회")
    @GetMapping("/records/{recordId}")
    fun getRecord(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @PathVariable recordId: Long
    ): ResponseEntity<RecordResponse> {
        val record = recordService.findRecordById(recordId, authTokenPayload.id)
        val response = RecordResponse(
            requireNotNull(record.id),
            record.fileKey.toUrl(),
            record.createdDate,
            record.feedbacks.map {
                FeedbackResponse(
                    id = requireNotNull(it.id),
                    teacher = it.teacher,
                    recordId = it.recordId.toNonNull(),
                    detail = it.detail,
                    createdAt = it.updatedDate
                )
            }
        )

        return ResponseEntity.ok(response)
    }

    @Auth
    @Operation(summary = "record 삭제")
    @DeleteMapping("/records/{recordId}")
    fun deleteRecord(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @PathVariable recordId: Long
    ): ResponseEntity<Unit> {
        recordService.deleteRecord(recordId, authTokenPayload.id)
        return ResponseEntity.ok().build()
    }

    @Auth
    @Operation(summary = "record 수정")
    @PatchMapping("/records/{recordId}")
    fun updateRecord(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @PathVariable recordId: Long,
        @RequestBody request: UpdateRecordRequest
    ): ResponseEntity<Unit> {
        recordService.updateRecord(recordId, authTokenPayload.id, request.title)
        return ResponseEntity.ok().build()
    }

    @Auth
    @Operation(summary = "feedback ticket 다건 조회")
    @GetMapping("/feedbacks/tickets")
    fun getAllFeedbacks(): ResponseEntity<List<FeedbackResponse>> {
        val response = recordService.findAllFeedbackDetails().map {
            FeedbackResponse(
                id = requireNotNull(it.id),
                teacher = it.teacher,
                recordId = it.recordId.toNonNull(),
                detail = it.detail,
                createdAt = it.updatedDate
            )
        }

        return ResponseEntity.ok(response)
    }

    @Auth
    @Operation(summary = "feedback ticket 단건 조회")
    @GetMapping("/feedbacks/tickets/{ticketId}")
    fun getFeedbackTicket(
        @PathVariable ticketId: Long
    ): ResponseEntity<FeedbackTicket> {
        return ResponseEntity.ok(recordService.findFeedbackTicket(ticketId))
    }

    private fun String.toUrl() = "$cloudfrontUrl/$this"
}
