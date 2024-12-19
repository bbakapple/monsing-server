package org.monsing.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.monsing.auth.jwt.TokenManager
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

private const val BEARER = "Bearer "

@Component
class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val authContext: AuthContext
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }

        if (handler.hasMethodAnnotation(Auth::class.java)) {
            val token = request.getHeader(HttpHeaders.AUTHORIZATION)
                .takeIf { it.startsWith(BEARER) }
                ?.apply { removePrefix(BEARER) }
                ?: throw IllegalArgumentException("잘못된 인증 요청")

            val payload = tokenManager.getPayLoad(token)
            authContext.payload = payload
        }

        return true
    }
}
