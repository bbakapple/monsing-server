package org.monsing.common.log

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRawValue
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
            val requestBodyString = request.contentAsString
            requestBody = if (requestBodyString.isNotBlank()) tryParseJson(requestBodyString) else null
            start = System.currentTimeMillis()
        }
    }

    fun setResponse(response: ContentCachingResponseWrapper) {
        metadata.get().apply {
            end = System.currentTimeMillis()
            status = response.status
            val responseBodyString = response.contentAsByteArray.toString(charset("UTF-8"))
            responseBody = if (responseBodyString.isNotBlank()) tryParseJson(responseBodyString) else null
        }
    }

    private fun tryParseJson(content: String): String {
        return try {
            if (content.trim().startsWith("{") || content.trim().startsWith("[")) {
                // 유효한 JSON인지 확인하고 예쁘게 포맷팅
                val jsonNode = objectMapper.readTree(content)
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode)
            } else {
                content
            }
        } catch (e: Exception) {
            content
        }
    }

    fun setException(ex: Exception) {
        val stackTraceLines = ex.stackTrace.take(ERROR_TRACE_LINE_NUMBER).map { it.toString() }
        
        metadata.get().apply {
            // 구조화된 예외 정보 저장
            exception = LogExceptionData(
                type = ex.javaClass.simpleName,
                message = ex.message ?: "No message",
                stackTrace = stackTraceLines
            )
        }
    }

    fun log() {
        // 로그 출력 시 새 줄에서 시작하도록 줄바꿈 추가
        val formattedLog = "\n" + objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(metadata.get())
        
        when (metadata.get().status) {
            HttpStatus.BAD_REQUEST.value() -> logger.warning(formattedLog)
            HttpStatus.INTERNAL_SERVER_ERROR.value() -> logger.severe(formattedLog)
            else -> logger.info(formattedLog)
        }
        
        metadata.remove()
        metadata.set(LogMetadata(profile = profile))
    }

    fun logException(e: Exception) {
        setException(e)
        
        // 로그 출력 시 새 줄에서 시작하도록 줄바꿈 추가
        val formattedLog = "\n" + objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(metadata.get())
        
        logger.severe(formattedLog)
        
        metadata.remove()
        metadata.set(LogMetadata(profile = profile))
    }
}

data class LogMetadata(
    val id: String = UUID.randomUUID().toString(),
    val profile: String?,
    var method: String? = null,
    var url: String? = null,
    @JsonRawValue // JSON 문자열을 그대로 출력
    var requestBody: String? = null,
    var headers: Map<String, String> = emptyMap(),
    var status: Int? = null,
    @JsonIgnore
    var start: Long? = null,
    @JsonIgnore
    var end: Long? = null,
    @JsonRawValue // JSON 문자열을 그대로 출력
    var responseBody: String? = null,
    var exception: LogExceptionData? = null
) {
    @get:JsonProperty
    private val duration: String
        get() = "${requireNotNull(end) - requireNotNull(start)}ms"
}

data class LogExceptionData(
    val type: String,
    val message: String,
    val stackTrace: List<String>
) 