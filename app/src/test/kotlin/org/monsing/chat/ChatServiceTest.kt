package org.monsing.chat

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.monsing.chat.session.GlobalServerIdStorage
import org.monsing.chat.session.LocalSessionStorage


class ChatServiceTest {

    private val localSessionStorage = mockk<LocalSessionStorage>(relaxed = true)
    private val globalServerIdStorage = mockk<GlobalServerIdStorage>(relaxed = true)
    private val memberChatRepository = mockk<MemberChatRepository>(relaxed = true)
    private val messageRepository = mockk<MessageRepository>(relaxed = true)
    private val objectMapper = mockk<ObjectMapper>(relaxed = true)
    private val chatService = spyk(
        objToCopy = ChatService(
            localSessionStorage = localSessionStorage,
            globalServerIdStorage = globalServerIdStorage,
            memberChatRepository = memberChatRepository,
            messageRepository = messageRepository,
            objectMapper = objectMapper
        )
    )

    @Test
    fun `채팅방 생성 시 참여인원 만큼 join 호출`() {
        chatService.createChat(1, 2, 3)

        verify(exactly = 3) { chatService.joinChat(any(), any()) }
    }
}
