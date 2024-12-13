package org.monsing.application

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.monsing.domain.Chat
import org.monsing.domain.MemberChat
import org.monsing.domain.MemberChatRepository
import org.monsing.domain.Message
import org.monsing.domain.MessageRepository
import org.monsing.domain.session.GlobalServerIdStorage
import org.monsing.domain.session.LocalSessionStorage
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

    fun relayMessage(receiverId: Long, message: Message) {
        localSessionStorage.getSession(receiverId)?.let {
            it.forEach {
                it.sendMessage(message.toPayload())
            }
        } ?: publishMessageSentEvent()
    }

    private fun publishMessageSentEvent() {
        TODO("Not yet implemented")
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
            globalServerIdStorage.saveServerId(it, session.localAddress.toString())
        }
    }

    fun removeSession(session: WebSocketSession) {
        session.attributes["memberId"]?.let {
            localSessionStorage.removeSession(it as Long, session)
            globalServerIdStorage.removeServerId(it, session.localAddress.toString())
        }
    }

    fun handleMessage(senderId: Long, message: WebSocketMessage<*>) {
        //TODO: message DTO로 수정해야함
        val msg = objectMapper.readValue(message.payload as String, Message::class.java)

        messageRepository.save(msg)

        sendMessage(msg)
    }


    private fun sendMessage(message: Message) {
        val receivers = memberChatRepository.findReceiverIdByChatId(message.chatId, message.senderId)

        for (receiver in receivers) {
            localSessionStorage.getSession(receiver)?.let {
                it.forEach {
                    it.sendMessage(message.toPayload())
                }
            } ?: globalServerIdStorage.getServerId(receiver)?.let {
                it.forEach {
                    sendOtherServer(it, receiver, message)
                }
            } ?: publishMessageSentEvent()
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

    private fun Message.toPayload() = TextMessage(objectMapper.writeValueAsString(this))
}
