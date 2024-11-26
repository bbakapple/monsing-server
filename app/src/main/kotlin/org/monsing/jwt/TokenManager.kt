package org.monsing.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class TokenManager(
    @Value("\${jwt.access-key}") accessKey: String,
    @Value("\${jwt.expire-second}") private val expireSecond: Long,
    private val objectMapper: ObjectMapper
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(accessKey.toByteArray())

    fun getPayLoad(token: String): TokenPayload {
        val parser = Jwts.parser()
            .verifyWith(key)
            .build()
        try {
            return parser.parseSignedClaims(token)
                .payload
                .subject
                .let { objectMapper.readValue(it, TokenPayload::class.java) }
        } catch (e: ExpiredJwtException) {
            throw ExpiredJwtException(e.header, e.claims, e.message)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid token")
        }
    }

    fun createToken(payload: TokenPayload): String {
        val issuedAt = Date()
        val expiration = issuedAt.toInstant().plusSeconds(expireSecond).let { Date.from(it) }
        return Jwts.builder()
            .subject(payload.id.toString())
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }
}

