package org.monsing.application

import org.monsing.domain.Message
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val webSocketHandler: WebSocketHandler
) {

    fun relayMessage(receiverId: Long, message: Message) {
        webSocketHandler.relayMessage(receiverId, message)
    }
}
