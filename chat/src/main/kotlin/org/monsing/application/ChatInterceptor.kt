package org.monsing.application

import org.monsing.auth.jwt.TokenManager
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class ChatInterceptor(private val tokenManager: TokenManager) : HandshakeInterceptor {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        request.headers["Authorization"]?.firstOrNull()
            ?.let {
                val token = it.substringAfter("Bearer ")
                tokenManager.getPayLoad(token).let {
                    attributes["memberId"] = it.id
                }
            } ?: return false
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