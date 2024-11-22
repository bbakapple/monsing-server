package org.monsing.auth

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.monsing.AuthService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Aspect
@Component
class AuthAspect(
    private val httpServletRequest: HttpServletRequest,
    private val authService: AuthService
) {

    @Before("@annotation(Auth)")
    fun authenticate(joinPoint: JoinPoint) {
        val authHeader = httpServletRequest.getHeader("Authorization")
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing")

        if (!authHeader.startsWith("Bearer ")) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Authorization header format")
        }

        val token = authHeader.removePrefix("Bearer ").trim()

        if (!authService.validateToken(token)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token")
        }

        // TODO: 추가 작업
    }
}
