package org.monsing.common.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

@Component
class LoggingFilter(
    private val httpLogger: HttpLogger
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        // 필터 체인 실행
        filterChain.doFilter(wrappedRequest, wrappedResponse)

        // 필터 체인이 실행된 후에 요청 정보와 응답 정보를 설정
        // 이 시점에서는 요청 본문이 이미 읽혀서 캐싱되어 있음
        httpLogger.setRequest(wrappedRequest)
        httpLogger.setResponse(wrappedResponse)

        // 응답 본문 복사
        if (!wrappedResponse.isCommitted) {
            wrappedResponse.copyBodyToResponse()
        }

        // 로깅 수행
        httpLogger.log()
    }
} 