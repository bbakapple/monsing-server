package org.monsing.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.monsing.auth.jwt.TokenManager
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val authContext: AuthContext
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        handler as HandlerMethod

        if (handler.hasMethodAnnotation(Auth::class.java)) {
            val token = request.getHeader("Authorization")
                .takeIf { it.startsWith("Bearer ") }
                ?.apply { removePrefix("Bearer ") }
                ?: throw IllegalArgumentException("잘못된 인증 요청")

            val payload = tokenManager.getPayLoad(token)
            authContext.payload = payload
        }

        return true
    }
}
