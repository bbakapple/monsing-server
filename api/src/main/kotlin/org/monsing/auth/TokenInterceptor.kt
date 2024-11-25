package org.monsing.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.monsing.jwt.TokenManager
import org.monsing.jwt.TokenManager.TokenStatus.*
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class TokenInterceptor(
    private val tokenManager: TokenManager,
    private val authContext: AuthContext
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
            ?: throw IllegalArgumentException("No token provided")

        val tokenStatus = tokenManager.validateAccessToken(token)

        when (tokenStatus) {
            VALID -> {
                val payload = tokenManager.getPayLoad(token)
                authContext.payload = payload
            }

            INVALID -> throw IllegalArgumentException("Invalid token")
            EXPIRED -> throw ExpiredTokenException("Token expired")
        }

        return true
    }
}
