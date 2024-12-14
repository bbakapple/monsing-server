package org.monsing.domain

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component

@Component
class MessageRepository(
    private val mongoTemplate: MongoTemplate,
    private val messageIdStrategy: MessageIdStrategy
) {

    fun save(message: Message) {
        messageIdStrategy.generateId(message)
        mongoTemplate.save(message, "message")
    }

    fun findByChatId(chatId: String, lastId: String, limit: Int): List<Message> {
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
}
