package org.monsing.application

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.monsing.chat.ChatService
import org.monsing.chat.MemberChatRepository
import org.monsing.chat.Message
import org.monsing.chat.MessageDto
import org.monsing.chat.MessageRepository
import org.monsing.chat.session.GlobalServerIdStorage
import org.monsing.chat.session.LocalSessionStorage
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

    private val objectMapper = mockk<ObjectMapper>(relaxed = true) {
        every { readValue(any(String::class), any<Class<*>>()) } returns MessageDto("1", "Hello")
    }

    private val chatService = spyk<ChatService>(
        objToCopy = ChatService(
            localSessionStorage,
            globalSessionStorage,
            memberChatRepository,
            messageRepository,
            objectMapper
        ),

        recordPrivateCalls = true
    )

    @Test
    fun `local의 session이 null이라면 global에서 id를 찾는다`() {
        // given
        val receiverId = 1L
        val message = objectMapper.writeValueAsString(MessageDto("1", "Hello"))
        every { localSessionStorage.getSessionByMemberId(receiverId) } returns null
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
        every { localSessionStorage.getSessionByMemberId(receiverId) } returns setOf()
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
        every { localSessionStorage.getSessionByMemberId(receiverId) } returns null
        every { globalSessionStorage.getServerId(receiverId) } returns null
        // when
        chatService.handleMessage(receiverId, TextMessage(message))

        // then
        verifyOrder {
            localSessionStorage.getSessionByMemberId(receiverId)
            globalSessionStorage.getServerId(receiverId)
            chatService["publishMessageSentEvent"]()
        }
    }

    @Test
    fun `local에 session이 있을 때 global은 호출하지 않는다`() {
        // given
        val receiverId = 1L
        every { localSessionStorage.getSessionByMemberId(receiverId) } returns setOf(mockk<WebSocketSession>(relaxed = true))
        // when
        chatService.handleMessage(receiverId, TextMessage(""))

        // then
        verify(exactly = 0) { globalSessionStorage.getServerId(receiverId) }
    }

    @Test
    fun `global에 serverId가 있을 때 다른 서버로 메시지를 전송한다`() {
        // given
        val receiverId = 1L
        every { objectMapper.writeValueAsString(any()) } returns ""
        every { localSessionStorage.getSessionByMemberId(receiverId) } returns null
        every { globalSessionStorage.getServerId(receiverId) } returns setOf("serverId")
        every {
            chatService["sendToOtherServer"](
                any(String::class),
                any(Long::class),
                any(Message::class)
            )
        } returns Unit
        // when
        chatService.handleMessage(receiverId, TextMessage(""))

        // then
        verify {
            chatService["sendToOtherServer"](
                any(String::class),
                any(Long::class),
                any(Message::class)
            )
        }
    }

    @Test
    fun `message를 relay할 때 local에 session이 있다면 message를 전송한다`() {
        // given
        val receiverId = 1L
        val session = mockk<WebSocketSession>(relaxed = true)
        every { localSessionStorage.getSessionByMemberId(receiverId) } returns setOf(session)
        // when
        chatService.handleMessage(receiverId, TextMessage(""))

        // then
        verify { session.sendMessage(any()) }
    }

    @Test
    fun `message를 relay할 때 local에 session이 없다면 event를 발행한다`() {
        // given
        val receiverId = 1L
        every { localSessionStorage.getSessionByMemberId(receiverId) } returns null
        every { globalSessionStorage.getServerId(receiverId) } returns null
        // when
        chatService.handleMessage(receiverId, TextMessage(""))

        // then
        verify { chatService["publishMessageSentEvent"]() }
    }
}
