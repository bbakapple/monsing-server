package org.monsing.domain

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class MessageRepositoryTest(
    @Autowired private val messageRepository: MessageRepository,
    @Autowired private val memberChatRepository: MemberChatRepository,
    @Autowired private val mongoTemplate: MongoTemplate
) {

    @BeforeEach
    fun clear() {
        mongoTemplate.dropCollection(Message::class.java)
        mongoTemplate.dropCollection(MemberChat::class.java)
        mongoTemplate.dropCollection(Chat::class.java)
    }

    @Test
    fun `메시지 저장 및 조회`() {
        messageRepository.save(Message(1, "1", 1, "Hello"))

        val messages = messageRepository.findByChatId("1", Long.MAX_VALUE, 10)

        messages[0].content shouldBe "Hello"
    }

    @Test
    fun `채팅 방에 따른 메시지를 찾는 쿼리 검증`() {
        messageRepository.save(Message(1, "1", 1, "Hello"))
        messageRepository.save(Message(2, "1", 1, "Hello"))

        val messages = messageRepository.findByChatId("1", Long.MAX_VALUE, 10)

        assertSoftly {
            messages.size shouldBe 2
            messages.map { it.id } shouldContainExactlyInAnyOrder setOf(1, 2)
        }
    }

    @Test
    fun `메시지 조회 쿼리 페이징 검증`() {
        for (i in 1..30) {
            messageRepository.save(Message(i.toLong(), "1", 1, "Hello$i"))
        }

        val messages = messageRepository.findByChatId("1", 20, 10)

        assertSoftly {
            messages.size shouldBe 10
            messages.map { it.id }.all { it!! < 20 } shouldBe true
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
}
