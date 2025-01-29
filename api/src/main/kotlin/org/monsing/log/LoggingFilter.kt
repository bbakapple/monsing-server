package org.monsing.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
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
        httpLogger.setRequest(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        filterChain.doFilter(request, wrappedResponse)

        httpLogger.setResponse(wrappedResponse, null)
        wrappedResponse.copyBodyToResponse()
        httpLogger.log()
    }
}
