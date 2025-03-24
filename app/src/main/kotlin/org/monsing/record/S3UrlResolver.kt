package org.monsing.record

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

interface S3UrlResolver {
    fun getObjectUrl(key: String): String
}

@Component
@Profile("!local")
class ProductionS3UrlResolver(
    @Value("\${s3.bucket}") private val bucket: String
) : S3UrlResolver {
    override fun getObjectUrl(key: String): String {
        return "https://$bucket.s3.amazonaws.com/$key"
    }
}

@Component
@Profile("local")
class LocalStackS3UrlResolver(
    @Value("\${s3.bucket}") private val bucket: String
) : S3UrlResolver {
    override fun getObjectUrl(key: String): String {
        return "http://localhost:4566/$bucket/$key"
    }
}
