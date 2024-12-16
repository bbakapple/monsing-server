package org.monsing.domain

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.ne
import org.springframework.stereotype.Component

@Component
class MemberChatRepository(private val mongoTemplate: MongoTemplate) {

    fun save(memberChat: MemberChat) {
        mongoTemplate.save(memberChat)
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
}
