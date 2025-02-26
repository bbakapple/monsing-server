package org.monsing.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.livekit.server.AccessToken
import io.livekit.server.RoomJoin
import io.livekit.server.RoomName
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class LiveKitTokenManager(
    @Value("\${livekit.api-key}") private val apiKey: String,
    @Value("\${livekit.secret.expire-second}") private val accessExpireSecond: Long,
    @Value("\${livekit.secret.key}") private val tokenSecret: String,
    private val objectMapper: ObjectMapper
) {

    fun generateToken(
        name: String,
        roomName: String,
        participantIdentity: String,
    ): String {
        val issuedAt = Date()
        val liveKitToken = AccessToken(apiKey, tokenSecret)
        liveKitToken.name = name
        liveKitToken.expiration = getExpiration(issuedAt, accessExpireSecond)
        liveKitToken.identity = participantIdentity
        liveKitToken.addGrants(
            RoomName(roomName),
            RoomJoin(true)
        )
        return liveKitToken.toJwt()
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

