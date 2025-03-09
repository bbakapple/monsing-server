package org.monsing.log

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.UUID
import java.util.logging.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

private const val ERROR_TRACE_LINE_NUMBER = 3

@Component
class HttpLogger(
    @Value("\${spring.profiles.active}") private val profile: String,
    private val objectMapper: ObjectMapper
) {
    private val metadata: ThreadLocal<LogMetadata> = ThreadLocal.withInitial { LogMetadata(profile = profile) }
    private val logger = Logger.getLogger(HttpLogger::class.simpleName)

    fun setRequest(request: ContentCachingRequestWrapper) {
        metadata.get().apply {
            url = request.requestURI
            method = request.method
            headers = request.headerNames.toList().associateWith { request.getHeader(it) }
            requestBody = request.contentAsString
            start = System.currentTimeMillis()
        }
    }

    fun setResponse(response: ContentCachingResponseWrapper) {
        metadata.get().apply {
            end = System.currentTimeMillis()
            status = response.status
            responseBody = response.contentAsByteArray.toString(charset("UTF-8"))
        }
    }

    fun setException(ex: Exception) {
        val message = StringBuilder().apply {
            appendLine("${ex.javaClass}: ${ex.message}")
            ex.stackTrace.take(ERROR_TRACE_LINE_NUMBER).forEach {
                appendLine(it)
            }
        }
        metadata.get().apply {
            exception = message.toString()
        }
    }

    fun log() {
        when (metadata.get().status) {
            HttpStatus.BAD_REQUEST.value() -> logger.warning(metadata.get().log)
            HttpStatus.INTERNAL_SERVER_ERROR.value() -> logger.severe(metadata.get().log)
            else -> logger.info(metadata.get().log)
        }
        metadata.remove()
        metadata.set(LogMetadata(profile = profile))
    }

    private val LogMetadata.log: String
        get() = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(this)
}

data class LogMetadata(
    val id: String = UUID.randomUUID().toString(),
    val profile: String?,
    var method: String? = null,
    var url: String? = null,
    var requestBody: String? = null,
    var headers: Map<String, String> = emptyMap(),
    var status: Int? = null,
    @JsonIgnore
    var start: Long? = null,
    @JsonIgnore
    var end: Long? = null,
    var responseBody: String? = null,
    var exception: String? = null
) {
    @get:JsonProperty
    private val duration: String
        get() = "${requireNotNull(end) - requireNotNull(start)}ms"
}
