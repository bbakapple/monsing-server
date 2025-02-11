package org.monsing.log

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

        httpLogger.setRequest(wrappedRequest)
        filterChain.doFilter(wrappedRequest, wrappedResponse)

        httpLogger.setResponse(wrappedResponse)
        wrappedResponse.copyBodyToResponse()
        httpLogger.log()
    }
}
