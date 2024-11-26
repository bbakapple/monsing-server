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
    @Value("\${jwt.access-token.secret}") accessSecret: String,
    @Value("\${jwt.access-token.expire-second}") private val accessExpireSecond: Long,
    @Value("\${jwt.access-token.secret}") refreshSecret: String,
    @Value("\${jwt.access-token.expire-second}") private val refreshExpireSecond: Long,
    private val objectMapper: ObjectMapper
) {

    private val accessKey: SecretKey = Keys.hmacShaKeyFor(accessSecret.toByteArray())
    private val refreshKey: SecretKey = Keys.hmacShaKeyFor(refreshSecret.toByteArray())

    fun getPayLoad(token: String): TokenPayload {
        val parser = Jwts.parser()
            .verifyWith(accessKey)
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

    fun createAccessToken(payload: TokenPayload): String {
        val issuedAt = Date()
        val expiration = issuedAt.toInstant().plusSeconds(accessExpireSecond).let { Date.from(it) }
        return Jwts.builder()
            .subject(payload.id.toString())
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(accessKey)
            .compact()
    }

    fun createRefreshToken(id: Long): String {
        val issuedAt = Date()
        val expiration = issuedAt.toInstant().plusSeconds(refreshExpireSecond).let { Date.from(it) }
        return Jwts.builder()
            .subject(id.toString())
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(refreshKey)
            .compact()
    }

    fun getRefreshPayload(token: String): Long {
        val parser = Jwts.parser()
            .verifyWith(refreshKey)
            .build()
        try {
            return parser.parseSignedClaims(token)
                .payload
                .subject
                .toLong()
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid token")
        }
    }
}

