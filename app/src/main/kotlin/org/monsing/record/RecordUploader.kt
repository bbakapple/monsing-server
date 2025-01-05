package org.monsing.record

import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Component
class RecordUploader(
    private val s3Client: S3Client,
    @Value("\${s3.bucket}") private val bucket: String
) {

    fun uploadRecord(file: MultipartFile): String {
        val key = createKey(file.originalFilename)
        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(file.bytes))

        return key
    }

    private fun createKey(originalFileName: String?): String {
        return "${LocalDateTime.now()}$originalFileName"
    }
}
