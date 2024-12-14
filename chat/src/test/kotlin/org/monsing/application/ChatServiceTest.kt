package org.monsing.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.monsing.domain.MemberChatRepository
import org.monsing.domain.MessageRepository
import org.monsing.domain.session.GlobalServerIdStorage
import org.monsing.domain.session.LocalSessionStorage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@ExtendWith(MockKExtension::class)
class ChatServiceTest {

    private val localSessionStorage = mockk<LocalSessionStorage>()
    private val globalSessionStorage = mockk<GlobalServerIdStorage>(
        relaxed = true
    )
    private val memberChatRepository = mockk<MemberChatRepository>() {
        every { findReceiverIdByChatId(any(), any()) } returns listOf(1L)
    }
    private val messageRepository = mockk<MessageRepository>(
        relaxed = true
    )

    private val objectMapper = spyk<ObjectMapper>().registerKotlinModule()

    private val chatService = ChatService(
        localSessionStorage,
        globalSessionStorage,
        memberChatRepository,
        messageRepository,
        objectMapper
    )
//    private val chatService = spyk<ChatService>(recordPrivateCalls = true)

    @Test
    fun `local의 session이 null이라면 global에서 id를 찾는다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        every { localSessionStorage.getSession(receiverId) } returns null
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
        verify { globalSessionStorage.getServerId(receiverId) }
    }

    @Test
    fun `local의 session이 빈 set이라면 global에서 id를 찾는다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        every { localSessionStorage.getSession(receiverId) } returns setOf()
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
        verify { globalSessionStorage.getServerId(receiverId) }
    }

    @Test
    fun `저장된 session이 없을 때 로직 순서를 검증한다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        every { localSessionStorage.getSession(receiverId) } returns null
        every { globalSessionStorage.getServerId(receiverId) } returns null
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
        verifyOrder {
            localSessionStorage.getSession(receiverId)
            globalSessionStorage.getServerId(receiverId)
        }
    }

    @Test
    fun `local에 session이 있을 때 global은 호출하지 않는다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        every { localSessionStorage.getSession(receiverId) } returns setOf(mockk<WebSocketSession>(relaxed = true))
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
        verify(exactly = 0) { globalSessionStorage.getServerId(receiverId) }
    }

    @Test
    fun `global에 serverId가 있을 때 다른 서버로 메시지를 전송한다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        every { localSessionStorage.getSession(receiverId) } returns null
        every { globalSessionStorage.getServerId(receiverId) } returns setOf("serverId")
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
//        verify { chatService invoke "sendToOtherServer" }
    }

    @Test
    fun `message를 relay할 때 local에 session이 있다면 message를 전송한다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        val session = mockk<WebSocketSession>(relaxed = true)
        every { localSessionStorage.getSession(receiverId) } returns setOf(session)
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
        verify { session.sendMessage(any()) }
    }

    @Test
    fun `message를 relay할 때 local에 session이 없다면 event를 발행한다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        every { localSessionStorage.getSession(receiverId) } returns null
        every { globalSessionStorage.getServerId(receiverId) } returns null
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
//        verify { chatService invoke "publishMessageSentEvent" }
    }
}
