package org.monsing.api

import org.monsing.auth.jwt.TokenManager
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

private const val BEARER = "Bearer "

@Component
class ChatInterceptor(private val tokenManager: TokenManager) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val memberId = request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()
            ?.let { tokenManager.getPayLoad(it.substringAfter(BEARER)).id }
            ?: throw IllegalArgumentException("Member id must not be null")

        val deviceId = requireNotNull(request.headers["Device-Id"]?.firstOrNull()) {
            "Device id must not be null"
        }

        attributes[MEMBER_METADATA] = MemberMetadata(memberId, deviceId)

        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        println("afterHandshake")
    }
}
