package org.monsing.log

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import java.util.UUID
import java.util.logging.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class HttpLogger(
    @Value("\${spring.profiles.active}") private val profile: String,
    private val objectMapper: ObjectMapper
) {
    private val metadata: ThreadLocal<LogMetadata> = ThreadLocal()
    private val logger = Logger.getLogger(HttpLogger::class.simpleName)

    fun init(request: HttpServletRequest) {
        metadata.set(
            LogMetadata(
                url = request.requestURI,
                method = request.method,
                profile = profile,
                headers = request.headerNames.toList().associateWith { request.getHeader(it) },
                requestBody = request.reader.readText()
            )
        )
    }

    fun setResponse(response: ContentCachingResponseWrapper, ex: Exception?) {
        metadata.get().apply {
            end = System.currentTimeMillis()
            status = response.status
            responseBody = ex?.let {
                "${ex.javaClass}: ${ex.message}"
            } ?: response.contentAsByteArray.toString()
        }
    }

    fun log() {
        when (metadata.get().status) {
            200 -> logger.info(metadata.get().log)
            400 -> logger.warning(metadata.get().log)
            else -> logger.severe(metadata.get().log)
        }
        metadata.remove()
    }

    private val LogMetadata.log: String
        get() = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this)
}

data class LogMetadata(
    val id: String = UUID.randomUUID().toString(),
    val profile: String,
    val method: String,
    val url: String,
    val requestBody: String,
    val headers: Map<String, String> = emptyMap(),
    var status: Int? = null,
    @JsonIgnore
    val start: Long = System.currentTimeMillis(),
    @JsonIgnore
    var end: Long? = null,
    var responseBody: String? = null,
) {
    @get:JsonProperty
    private val duration: String
        get() = "${requireNotNull(end) - start}ms"
}
