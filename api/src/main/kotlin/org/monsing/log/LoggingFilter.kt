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
        httpLogger.init(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)
        runCatching {
            filterChain.doFilter(request, wrappedResponse)
        }.onSuccess {
            httpLogger.setResponse(wrappedResponse, null)
        }.onFailure {
            httpLogger.setResponse(wrappedResponse, it as Exception)
        }.also {
            wrappedResponse.copyBodyToResponse()
            httpLogger.log()
        }
    }
}
