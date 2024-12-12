package org.monsing.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.monsing.domain.Message
import org.monsing.domain.MessageRepository
import org.monsing.domain.session.GlobalServerIdStorage
import org.monsing.domain.session.LocalSessionStorage
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class WebSocketHandler(
    private val globalServerIdStorage: GlobalServerIdStorage,
    private val localSessionStorage: LocalSessionStorage,
    private val objectMapper: ObjectMapper,
    private val messageRepository: MessageRepository
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.attributes["memberId"]?.let {
            localSessionStorage.saveSession(it as Long, session)
            globalServerIdStorage.saveServerId(it, session.localAddress.toString())
        }
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        val msg = objectMapper.readValue(message.payload as String, Message::class.java)

        messageRepository.save(msg)

        sendMessage(msg)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        session.attributes["memberId"]?.let {
            localSessionStorage.removeSession(it as Long, session)
            globalServerIdStorage.removeServerId(it, session.localAddress.toString())
        }
    }

    private fun sendMessage(message: Message) {
        val receivers = messageRepository.findReceiverIdByChatId(message.chatId, message.senderId)

        for (receiver in receivers) {
            localSessionStorage.getSession(receiver)?.let {
                it.forEach {
                    it.sendMessage(message.toPayload())
                }
            } ?: globalServerIdStorage.getServerId(receiver)?.let {
                it.forEach {
                    sendOtherServer(it, receiver, message)
                }
            } ?: alert(receiver)
        }
    }

    fun relayMessage(receiverId: Long, message: Message) {
        val sessions = localSessionStorage.getSession(receiverId)

        sessions?.forEach {
            it.sendMessage(message.toPayload())
        }
    }

    private fun sendOtherServer(receiverServerId: String, receiverId: Long, message: Message) {
        val client = HttpClient.newHttpClient()
        client.sendAsync(
            HttpRequest.newBuilder()
                .uri(URI.create("http:/$receiverServerId/relay?receiverId=$receiverId"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(message)))
                .build(),
            HttpResponse.BodyHandlers.ofString()
        )
    }

    fun alert(receiverId: Long) {
        //TODO:알림 전송
    }

    private fun Message.toPayload() = TextMessage(objectMapper.writeValueAsString(this))
}

