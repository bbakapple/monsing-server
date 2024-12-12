package org.monsing.domain

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class MessageRepository(private val mongoTemplate: MongoTemplate) {

    fun save(message: Message) = mongoTemplate.save(message, "message")

    fun findByChatId(chatId: Long, lastId: Long, limit: Int): List<Message> {
        val query = Query().addCriteria(
            where("id").lt(lastId)
                .and("chatId").`is`(chatId)
        ).with(Sort.by(Sort.Direction.DESC, "id"))
            .limit(limit)

        return mongoTemplate.find(
            query,
            Message::class.java,
            "message"
        )
    }

    fun findReceiverIdByChatId(chatId: Long, senderId: Long): List<Long> {
        val query = Query().addCriteria(
            where("chatId").`is`(chatId)
        )

        return mongoTemplate.find(
            query,
            MemberChat::class.java,
            "member_chat"
        ).filter { it.memberId != senderId }
            .map { it.memberId }
    }
}
