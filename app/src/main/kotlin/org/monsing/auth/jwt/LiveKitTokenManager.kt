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
        val payload = LiveKitPayload(
            name = name,
            identity = participantIdentity,
            grants = LiveKitPayload.Grants(
                room = LiveKitPayload.Room(
                    join = true,
                    name = roomName
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

data class LiveKitPayload(
    val name: String,
    val identity: String,
    val metadata: Any? = null,
    val grants: Grants
) {

    data class Grants(
        val room: Room
    )

    data class Room(
        val join: Boolean,
        val name: String
    )
}

