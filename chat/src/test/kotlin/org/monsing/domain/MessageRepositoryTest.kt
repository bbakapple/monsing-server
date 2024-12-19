package org.monsing.domain

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.monsing.chat.Chat
import org.monsing.chat.MemberChat
import org.monsing.chat.MemberChatRepository
import org.monsing.chat.Message
import org.monsing.chat.MessageRepository
import org.monsing.support.SpringBootTestWithRedis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

class MessageRepositoryTest(
    @Autowired private val messageRepository: MessageRepository,
    @Autowired private val memberChatRepository: MemberChatRepository,
    @Autowired private val mongoTemplate: MongoTemplate
) : SpringBootTestWithRedis() {

    @BeforeEach
    fun clear() {
        mongoTemplate.dropCollection(Message::class.java)
        mongoTemplate.dropCollection(MemberChat::class.java)
        mongoTemplate.dropCollection(Chat::class.java)
    }

    @Test
    fun `메시지 저장 및 조회`() {
        messageRepository.save(Message(chatId = "1", senderId = 1, content = "Hello"))

        val messages = messageRepository.findByChatId("1", "9".repeat(20), 10)

        messages[0].content shouldBe "Hello"
    }

    @Test
    fun `채팅 방에 따른 메시지를 찾는 쿼리 검증`() {
        messageRepository.save(Message(chatId = "1", senderId = 1, content = "Hello"))
        messageRepository.save(Message(chatId = "1", senderId = 1, content = "Hello"))
        messageRepository.save(Message(chatId = "2", senderId = 1, content = "Hello"))

        val messages = messageRepository.findByChatId("1", "9".repeat(20), 10)

        assertSoftly {
            messages.size shouldBe 2
        }
    }

    @Test
    fun `메시지 조회 쿼리 페이징 검증`() {
        var bound: String = ""

        for (i in 1..30) {
            val message = Message(chatId = "1", senderId = 1, content = "Hello$i")
            messageRepository.save(message)
            if (i == 20) {
                bound = message.id!!
            }
        }

        val messages = messageRepository.findByChatId("1", bound, 10)

        assertSoftly {
            messages.size shouldBe 10
            messages.map { it.id }.all { it!! < bound } shouldBe true
        }
    }

    @Test
    fun `수신자를 찾는 쿼리 검증`() {
        //given 1번 채팅에 1,2번 유저 참가
        memberChatRepository.save(MemberChat("1", 1, "1"))
        memberChatRepository.save(MemberChat("2", 2, "1"))


        //when 2번 유저가 1번 채팅에서 수신자를 찾음
        val receivers = memberChatRepository.findReceiverIdByChatId("1", 2)

        assertSoftly {
            receivers shouldContainOnly setOf(1)
        }
    }

    @Test
    fun `나중에 저장된 message의 id가 더 크다`() {
        val message1 = Message(chatId = "1", senderId = 1, content = "Hello1")
        val message2 = Message(chatId = "1", senderId = 1, content = "Hello2")
        messageRepository.save(message1)
        messageRepository.save(message2)

        message2.id?.shouldBeGreaterThan(message1.id!!)
    }

    @Test
    fun `동시에 저장된 message의 id가 구분된다`() {
        val message1 = Message(chatId = "1", senderId = 1, content = "Hello1")
        val message2 = Message(chatId = "1", senderId = 1, content = "Hello2")
        Thread().run {
            messageRepository.save(message1)
        }
        Thread().run {
            messageRepository.save(message2)
        }

        message1.shouldNotBeEqual(message2)
    }
}
