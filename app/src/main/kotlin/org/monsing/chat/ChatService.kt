package org.monsing.chat

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.monsing.chat.session.GlobalServerIdStorage
import org.monsing.chat.session.LocalSessionStorage
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

@Service
class ChatService(
    private val localSessionStorage: LocalSessionStorage,
    private val globalServerIdStorage: GlobalServerIdStorage,
    private val memberChatRepository: MemberChatRepository,
    private val messageRepository: MessageRepository,
    private val objectMapper: ObjectMapper
) {

    private val client = HttpClient.newHttpClient()

    fun relayMessage(receiverId: Long, message: Message) {
        localSessionStorage.getSession(receiverId)?.let {
            it.forEach {
                it.sendMessage(message.toPayload())
            }
        } ?: publishMessageSentEvent()
    }

    private fun publishMessageSentEvent() {
        //TODO: Implement this method
    }

    fun createChat(memberIds: List<Long>): String {
        val chat = memberChatRepository.saveChat(Chat())
        val chatId = chat.id

        memberIds.forEach { memberId ->
            joinChat(chatId, memberId)
        }
        return chatId
    }

    fun joinChat(chatId: String, memberId: Long) {
        memberChatRepository.save(MemberChat(chatId = chatId, memberId = memberId))
    }

    fun leaveChat(chatId: String, memberId: Long) {
        memberChatRepository.deleteByChatIdAndMemberId(chatId, memberId)
    }


    fun saveSession(session: WebSocketSession) {
        session.attributes["memberId"]?.let {
            localSessionStorage.saveSession(it as Long, session)
            globalServerIdStorage.saveServerId(it, session.serverAddress())
        }
    }

    fun removeSession(session: WebSocketSession) {
        session.attributes["memberId"]?.let {
            localSessionStorage.removeSession(it as Long, session)
            globalServerIdStorage.removeServerId(it, session.serverAddress())
        }
    }

    fun handleMessage(senderId: Long, message: WebSocketMessage<*>) {
        val dto = objectMapper.readValue(message.payload as String, MessageDto::class.java)

        val msg = Message(chatId = dto.chatId, senderId = senderId, content = dto.content)

        messageRepository.save(msg)

        sendMessage(msg)
    }

    private fun sendMessage(message: Message) {
        val receivers = memberChatRepository.findReceiverIdByChatId(message.chatId, message.senderId)

        for (receiver in receivers) {
            val localSessions = localSessionStorage.getSession(receiver)

            localSessions?.let { session ->
                session.forEach {
                    it.sendMessage(message.toPayload())
                }
            }

            if (localSessions.isNullOrEmpty()) {
                findGlobalSessionAndSendMessage(receiver, message)
            }
        }
    }

    private fun findGlobalSessionAndSendMessage(receiver: Long, message: Message) {
        val globalServerIds = globalServerIdStorage.getServerId(receiver)

        globalServerIds?.let { id ->
            id.forEach {
                sendToOtherServer(it, receiver, message)
            }
        }

        if (globalServerIds.isNullOrEmpty()) {
            publishMessageSentEvent()
        }
    }

    private fun sendToOtherServer(receiverServerId: String, receiverId: Long, message: Message) {
        client.sendAsync(
            HttpRequest.newBuilder()
                .uri(URI.create("http://$receiverServerId/relay?receiverId=$receiverId"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(message)))
                .build(),
            HttpResponse.BodyHandlers.ofString()
        )
    }

    private fun Message.toPayload() = TextMessage(objectMapper.writeValueAsString(this))
    private fun WebSocketSession.serverAddress() = localAddress.toString().removePrefix("/")
}
