package org.monsing.member.block

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component

@Component
class BlockRepository(private val mongoTemplate: MongoTemplate) {

    fun save(blockerId: Long, blockedId: Long) {
        mongoTemplate.save(Block(blockerId = blockerId, blockedId = blockedId))
    }

    fun findByBlockerId(blockerId: Long): List<Block> {
        return mongoTemplate.find(
            Query().addCriteria(
                Block::blockerId isEqualTo blockerId
            ),
            Block::class.java
        )
    }

    fun existsByBlockerIdAndBlockedId(blockerId: Long, blockedId: Long): Boolean {
        return mongoTemplate.exists(
            Query().addCriteria(
                (Block::blockerId isEqualTo blockerId)
                    .andOperator(Block::blockedId isEqualTo blockedId)
            ),
            Block::class.java
        )
    }
}
