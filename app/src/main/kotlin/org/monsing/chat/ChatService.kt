package org.monsing.chat

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.monsing.chat.session.GlobalServerIdStorage
import org.monsing.chat.session.LocalSessionStorage
import org.monsing.member.block.BlockRepository
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
    private val blockRepository: BlockRepository,
    private val objectMapper: ObjectMapper
) {

    private val client = HttpClient.newHttpClient()

    fun relayMessage(receiverId: Long, message: Message) {
        localSessionStorage.getSessionByMemberId(receiverId)?.let { session ->
            session.forEach {
                it.sendMessage(message.toPayload())
            }
        } ?: publishMessageSentEvent()
    }

    private fun publishMessageSentEvent() {
        //TODO: Implement this method
    }

    fun createChat(receiverId: Long, senderId: Long): String {
        val chat = memberChatRepository.findChatBetweenTwoMembers(receiverId, senderId)
            ?: memberChatRepository.saveChat(Chat())
        val chatId = chat.id

        joinChat(chatId, receiverId, senderId)

        return chatId
    }

    fun joinChat(chatId: String, vararg memberIds: Long) {
        val memberChats = memberIds.map { MemberChat(chatId = chatId, memberId = it) }
        memberChatRepository.save(memberChats)
    }

    fun leaveChat(chatId: String, memberId: Long) {
        memberChatRepository.deleteByChatIdAndMemberId(chatId, memberId)
    }


    fun saveSession(memberId: Long, deviceId: String, session: WebSocketSession) {
        localSessionStorage.saveSession(memberId, deviceId, session)
        globalServerIdStorage.saveServerId(memberId, session.serverAddress())

    }

    fun removeSession(memberId: Long, deviceId: String) {
        localSessionStorage.removeSession(memberId, deviceId)

    }

    fun handleMessage(senderId: Long, message: WebSocketMessage<*>) {
        val dto = objectMapper.readValue(message.payload as String, MessageDto::class.java)
        val msg = Message(chatId = dto.chatId, senderId = senderId, content = dto.content)

        val receivers = memberChatRepository.findReceiverIdByChatId(msg.chatId, senderId)
            .filter { blockRepository.existsByBlockerIdAndBlockedId(it, senderId).not() }

        if (receivers.isEmpty()) {
            return
        }

        messageRepository.save(msg)

        sendMessage(msg, receivers)
    }

    private fun sendMessage(message: Message, receivers: List<Long>) {
        for (receiver in receivers) {
            val localSessions = localSessionStorage.getSessionByMemberId(receiver)

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

    fun getMessages(chatId: String, lastId: String?, size: Int?, memberId: Long): List<Message> {
        val isExists = memberChatRepository.existByChatId(chatId, memberId)
        require(isExists) {
            throw IllegalArgumentException("채팅방에 참여하지 않은 사용자입니다.")
        }
        return messageRepository.findByChatId(chatId, lastId, size)
    }

    fun findChatByMemberId(memberId: Long): List<Chat> {
        return memberChatRepository.findChatByMemberId(memberId)
    }

    fun findChatThumbnail(chatId: String, memberId: Long): ThumbnailDto {
        val opp = memberChatRepository.findOpponentId(chatId, memberId)
        val lastMessage = messageRepository.findLastMessageByChatId(chatId)

        return ThumbnailDto(
            chatId = chatId,
            opponentId = opp,
            message = lastMessage
        )
    }

    private fun Message.toPayload() = TextMessage(objectMapper.writeValueAsString(this))
    private fun WebSocketSession.serverAddress() = localAddress.toString().removePrefix("/")
}

data class ThumbnailDto(
    val chatId: String,
    val opponentId: Long,
    val message: Message?
)
