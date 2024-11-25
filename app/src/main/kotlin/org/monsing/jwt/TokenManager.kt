package org.monsing.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class TokenManager(
    @Value("\${jwt.access-key}") private val key: SecretKey
) {

    private val parser = Jwts.parser()
        .verifyWith(key)
        .build()

    fun validateAccessToken(token: String): TokenStatus {
        try {
            parser.parseSignedClaims(token)
        } catch (e: ExpiredJwtException) {
            return TokenStatus.EXPIRED
        } catch (e: Exception) {
            return TokenStatus.INVALID
        }
        return TokenStatus.VALID
    }

    fun getPayLoad(token: String): String {
        return parser.parseSignedClaims(token)
            .payload
            .subject
    }

    enum class TokenStatus {
        VALID,
        INVALID,
        EXPIRED
    }
}

