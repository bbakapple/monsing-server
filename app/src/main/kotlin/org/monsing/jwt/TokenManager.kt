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

    fun getPayLoad(token: String): TokenPayload {
        try {
            return parser.parseSignedClaims(token)
                .payload
                .let { TokenPayload(it["id"] as Long) }
        } catch (e: ExpiredJwtException) {
            throw ExpiredJwtException(e.header, e.claims, e.message)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid token")
        }
    }
}

