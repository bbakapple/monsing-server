package org.monsing.chat

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.ne
import org.springframework.stereotype.Component

@Component
class MemberChatRepository(private val mongoTemplate: MongoTemplate) {

    fun save(memberChat: MemberChat) {
        mongoTemplate.save(memberChat)
    }

    fun save(memberChats: List<MemberChat>) {
        mongoTemplate.save(memberChats)
    }

    fun deleteByChatIdAndMemberId(chatId: String, memberId: Long) {
        val query = Query().addCriteria(
            (MemberChat::chatId isEqualTo chatId)
                .andOperator(MemberChat::memberId isEqualTo memberId)
        )
        mongoTemplate.remove(
            query,
            MemberChat::class.java,
        )
    }

    fun findReceiverIdByChatId(chatId: String, senderId: Long): List<Long> {
        val query = Query().addCriteria(
            (MemberChat::chatId isEqualTo chatId)
                .andOperator(MemberChat::memberId ne senderId)
        )

        return mongoTemplate.find(
            query,
            MemberChat::class.java,
        ).map { it.memberId }
    }

    fun saveChat(chat: Chat): Chat {
        return mongoTemplate.save(chat)
    }

    fun existByChatId(chatId: String, memberId: Long): Boolean {
        val query = Query().addCriteria(
            (MemberChat::chatId isEqualTo chatId)
                .andOperator(MemberChat::memberId isEqualTo memberId)
        )

        return mongoTemplate.exists(
            query,
            MemberChat::class.java,
        )
    }

    fun findChatByMemberId(memberId: Long): List<Chat> {
        val query = Query().addCriteria(
            MemberChat::memberId isEqualTo memberId
        )

        val chatIds = mongoTemplate.find(
            query,
            MemberChat::class.java,
        ).map { it.chatId }

        return mongoTemplate.find(
            Query().addCriteria(
                Chat::id inValues chatIds
            ),
            Chat::class.java
        )
    }

    fun findChatBetweenTwoMembers(member1Id: Long, member2Id: Long): Chat? {
        val member1ChatIds = findChatIdsByMemberId(member1Id)
        val member2ChatIds = findChatIdsByMemberId(member2Id)

        val commonChatIds = member1ChatIds.intersect(member2ChatIds)

        if (commonChatIds.isEmpty()) {
            return null
        }

        return mongoTemplate.findOne(
            Query().addCriteria(
                Chat::id inValues commonChatIds
            ),
            Chat::class.java
        )
    }

    private fun findChatIdsByMemberId(memberId: Long) = mongoTemplate.find(
        Query().addCriteria(
            MemberChat::memberId isEqualTo memberId
        ),
        MemberChat::class.java
    ).map { it.chatId }

    fun findOpponentId(chatId: String, memberId: Long): Long {
        val query = Query().addCriteria(
            (MemberChat::chatId isEqualTo chatId)
                .andOperator(MemberChat::memberId ne memberId)
        )

        return mongoTemplate.findOne(
            query,
            MemberChat::class.java,
        )?.memberId ?: throw IllegalArgumentException("Opponent not found")
    }

    fun findLastReadMessageId(chatId: String, memberId: Long): String? {
        val query = Query().addCriteria(
            (MemberChat::chatId isEqualTo chatId)
                .andOperator(MemberChat::memberId ne memberId)
        )

        val opponentId = mongoTemplate.findOne(
            query,
            MemberChat::class.java,
        )?.memberId

        return mongoTemplate.findOne(
            Query().addCriteria(
                (MessageRead::chatId isEqualTo chatId)
                    .andOperator(MessageRead::memberId isEqualTo opponentId)
            ),
            MessageRead::class.java
        )?.messageId
    }

    fun saveLastReadMessageId(chatId: String, memberId: Long, messageId: String) {
        val query = Query().addCriteria(
            (MessageRead::chatId isEqualTo chatId)
                .andOperator(MessageRead::memberId isEqualTo memberId)
        )

        val messageRead = mongoTemplate.findOne(
            query,
            MessageRead::class.java,
        ) ?: MessageRead(
            chatId = chatId,
            memberId = memberId,
            messageId = messageId
        )

        messageRead.messageId = messageId

        mongoTemplate.save(messageRead)
    }
}
