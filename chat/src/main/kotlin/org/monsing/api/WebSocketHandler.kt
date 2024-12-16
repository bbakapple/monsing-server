package org.monsing.api

import org.monsing.chat.ChatService
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebSocketHandler(
    private val chatService: ChatService
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        chatService.saveSession(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        val senderId = requireNotNull(session.attributes["memberId"] as Long)

        chatService.handleMessage(senderId, message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        chatService.removeSession(session)
    }
}


