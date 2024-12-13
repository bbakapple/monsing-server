package org.monsing.domain

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Component

@Component
class MemberChatRepository(private val mongoTemplate: MongoTemplate) {

    fun save(memberChat: MemberChat) {
        mongoTemplate.save(memberChat, "member_chat")
    }

    fun deleteByChatIdAndMemberId(chatId: String, memberId: Long) {
        mongoTemplate.remove(
            query(
                where("chatId").`is`(chatId)
                    .and("memberId").`is`(memberId)
            ),
            MemberChat::class.java,
            "member_chat"
        )
    }

    fun findReceiverIdByChatId(chatId: String, senderId: Long): List<Long> {
        val query = Query().addCriteria(
            where("chatId").`is`(chatId)
                .and("memberId").ne(senderId)
        )

        return mongoTemplate.find(
            query,
            MemberChat::class.java,
            "member_chat"
        ).map { it.memberId }
    }

    fun saveChat(chat: Chat): Chat {
        return mongoTemplate.save(chat, "chat")
    }
}
