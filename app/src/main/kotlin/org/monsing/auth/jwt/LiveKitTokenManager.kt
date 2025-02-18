package org.monsing.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class LiveKitTokenManager(
    @Value("\${livekit.api-key}") apiKey: String,
    @Value("\${livekit.secret.expire-second}") private val accessExpireSecond: Long,
    @Value("\${livekit.secret.key}") tokenSecret: String,
    private val objectMapper: ObjectMapper
) {
    private val apiKey = apiKey
    private val accessKey: SecretKey = Keys.hmacShaKeyFor(tokenSecret.toByteArray())

    fun generateToken(
        name: String,
        roomName: String,
        participantIdentity: String,
    ): String {
        val issuedAt = Date()
        val expiration = getExpiration(issuedAt, accessExpireSecond)
        val payload = mapOf(
            "name" to name,
            "identity" to participantIdentity,
            "metadata" to null,
            "grants" to mapOf(
                "room" to mapOf(
                    "join" to true,
                    "name" to roomName
                )
            )
        )
        val subject = objectMapper.writeValueAsString(payload)
        return Jwts.builder()
            .issuer(apiKey)
            .subject(subject)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(accessKey)
            .compact()
    }

    private fun getExpiration(issuedAt: Date, expirationSecond: Long): Date {
        return issuedAt.toInstant().plusSeconds(expirationSecond).let { Date.from(it) }
    }

}
