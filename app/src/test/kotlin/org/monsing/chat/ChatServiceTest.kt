package org.monsing.chat

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.mockk
import io.mockk.spyk
import org.monsing.chat.session.GlobalServerIdStorage
import org.monsing.chat.session.LocalSessionStorage
import org.monsing.member.block.BlockRepository


class ChatServiceTest {

    private val localSessionStorage = mockk<LocalSessionStorage>(relaxed = true)
    private val globalServerIdStorage = mockk<GlobalServerIdStorage>(relaxed = true)
    private val memberChatRepository = mockk<MemberChatRepository>(relaxed = true)
    private val messageRepository = mockk<MessageRepository>(relaxed = true)
    private val objectMapper = mockk<ObjectMapper>(relaxed = true)
    private val unReadCountRepository = mockk<MessageUnReadCountRepository>(relaxed = true)
    private val blockRepository = mockk<BlockRepository>(relaxed = true)
    private val chatService = spyk(
        objToCopy = ChatService(
            localSessionStorage = localSessionStorage,
            globalServerIdStorage = globalServerIdStorage,
            memberChatRepository = memberChatRepository,
            messageRepository = messageRepository,
            unReadCountRepository = unReadCountRepository,
            objectMapper = objectMapper,
            blockRepository = blockRepository
        )
    )
}
