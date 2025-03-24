package org.monsing.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.HeadBucketRequest
import software.amazon.awssdk.services.s3.model.NoSuchBucketException

@Component
@Profile("local")
class LocalStackInitializer(
    private val s3Client: S3Client,
    @Value("\${s3.bucket}") private val bucket: String
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(LocalStackInitializer::class.java)

    override fun run(vararg args: String) {
        initializeS3Bucket()
    }

    private fun initializeS3Bucket() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build())
            logger.info("S3 버킷 '{}' 이미 존재합니다. (LocalStack)", bucket)
        } catch (e: NoSuchBucketException) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build())
            logger.info("S3 버킷 '{}' 생성되었습니다. (LocalStack)", bucket)
        } catch (e: Exception) {
            logger.error("LocalStack S3 버킷 초기화 중 오류 발생: {}", e.message, e)
            logger.warn("LocalStack이 실행 중인지 확인하세요. Docker Compose를 통해 실행할 수 있습니다.")
        }
    }
}
