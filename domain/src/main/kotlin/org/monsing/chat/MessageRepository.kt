package org.monsing.chat

import kotlin.reflect.KProperty
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.lt
import org.springframework.stereotype.Component

@Component
class MessageRepository(
    private val mongoTemplate: MongoTemplate,
    private val messageIdStrategy: MessageIdStrategy
) {

    fun save(message: Message) {
        messageIdStrategy.generateId(message)
        mongoTemplate.save(message)
    }

    fun findByChatId(chatId: String, lastId: String, limit: Int): List<Message> {
        val query = Query().addCriteria(
            (Message::id lt lastId)
                .andOperator(Message::chatId isEqualTo chatId)
        ).with(
            sortBy(Message::id, Sort.Direction.DESC)
        ).limit(limit)

        return mongoTemplate.find(
            query,
            Message::class.java
        )
    }

    fun sortBy(property: KProperty<*>, direction: Sort.Direction = Sort.Direction.ASC): Sort {
        return Sort.by(Sort.Order(direction, property.name))
    }
}
