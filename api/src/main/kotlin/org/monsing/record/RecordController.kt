//package org.monsing.record
//
//import org.monsing.auth.Auth
//import org.monsing.auth.AuthPayload
//import org.monsing.auth.jwt.AuthTokenPayload
//import org.monsing.record.request.RequestFeedbackRequest
//import org.monsing.record.request.UpdateRecordRequest
//import org.monsing.record.request.UploadRecordRequest
//import org.monsing.record.request.WriteFeedbackRequest
//import org.monsing.record.response.FeedbackResponse
//import org.monsing.record.response.RecordResponse
//import org.monsing.record.response.RecordUploadResponse
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.DeleteMapping
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PatchMapping
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestParam
//import org.springframework.web.bind.annotation.RequestPart
//import org.springframework.web.bind.annotation.RestController
//import org.springframework.web.multipart.MultipartFile
//
//@RestController
//class RecordController(
//    private val recordUploader: RecordUploader,
//    private val recordService: RecordService,
//    @Value("\${aws.cloudfront-url}") private val cloudfrontUrl: String
//) {
//
//    @Auth
//    @PostMapping("/records")
//    fun uploadRecord(
//        @RequestPart file: MultipartFile,
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @RequestBody request: UploadRecordRequest
//    ): ResponseEntity<RecordUploadResponse> {
//        val key = recordUploader.uploadRecord(file)
//        recordService.saveRecord(Record(request.title, authTokenPayload.id, key))
//
//        return ResponseEntity.ok(RecordUploadResponse(key))
//    }
//
//    @Auth
//    @PostMapping("/records/{recordId}/feedback")
//    fun requestFeedback(
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @PathVariable recordId: Long,
//        @RequestBody request: RequestFeedbackRequest
//    ): ResponseEntity<Unit> {
//        recordService.requestFeedback(authTokenPayload.id, recordId, request.teacherId)
//        return ResponseEntity.ok().build()
//    }
//
//    @Auth
//    @PatchMapping("/records/{recordId}/feedback")
//    fun writeFeedback(
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @PathVariable recordId: Long,
//        @RequestBody request: WriteFeedbackRequest
//    ): ResponseEntity<Unit> {
//        recordService.writeFeedback(authTokenPayload.id, recordId, request.detail)
//        return ResponseEntity.ok().build()
//    }
//
//    @Auth
//    @GetMapping("/records")
//    fun listRecords(
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @RequestParam(required = false) size: Int?,
//        @RequestParam(required = false) lastId: Long?
//    ): ResponseEntity<List<RecordResponse>> {
//        val records = recordService.findRecordsByMemberId(authTokenPayload.id, size, lastId)
//
//        val response = records.map {
//            RecordResponse(
//                id = requireNotNull(it.id),
//                url = it.fileKey.toUrl(),
//                createdAt = it.createdDate
//            )
//        }
//
//        return ResponseEntity.ok(response)
//    }
//
//    @Auth
//    @GetMapping("/records/{recordId}")
//    fun getRecord(
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @PathVariable recordId: Long
//    ): ResponseEntity<RecordResponse> {
//        val record = recordService.findRecordById(recordId, authTokenPayload.id, authTokenPayload.role)
//        val response = RecordResponse(
//            requireNotNull(record.id),
//            record.fileKey.toUrl(),
//            record.createdDate,
//            record.feedbacks.map {
//                FeedbackResponse(
//                    requireNotNull(it.id),
//                    it.teacherId,
//                    it.detail,
//                    it.updatedDate
//                )
//            }
//        )
//
//        return ResponseEntity.ok(response)
//    }
//
//    @Auth
//    @DeleteMapping("/records/{recordId}")
//    fun deleteRecord(
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @PathVariable recordId: Long
//    ): ResponseEntity<Unit> {
//        recordService.deleteRecord(recordId, authTokenPayload.id)
//        return ResponseEntity.ok().build()
//    }
//
//    @Auth
//    @PatchMapping("/records/{recordId}")
//    fun updateRecord(
//        @AuthPayload authTokenPayload: AuthTokenPayload,
//        @PathVariable recordId: Long,
//        @RequestBody request: UpdateRecordRequest
//    ): ResponseEntity<Unit> {
//        recordService.updateRecord(recordId, authTokenPayload.id, request.title)
//        return ResponseEntity.ok().build()
//    }
//
//    private fun String.toUrl() = "$cloudfrontUrl/$this"
//}
