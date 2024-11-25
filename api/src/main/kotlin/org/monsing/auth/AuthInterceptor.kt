package org.monsing.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.security.auth.login.CredentialException

@Component
class AuthInterceptor(private val tokenInterceptor: TokenInterceptor) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        handler as HandlerMethod

        if (handler.hasMethodAnnotation(Auth::class.java)) {
            val header = request.getHeader("Authorization") ?: throw CredentialException("No Authorization header")
            require(header.startsWith("Bearer ")) {
                "Invalid Authorization Header"
            }

            return tokenInterceptor.preHandle(request, response, handler)
        }

        return true
    }
}
