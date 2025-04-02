package org.monsing.chat

import org.monsing.config.toObject
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class MessageUnReadCountRepository(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun increment(chatId: String, memberId: Long) {
        redisTemplate.opsForValue().increment(key(chatId, memberId))
    }

    fun get(chatId: String, memberId: Long): Int {
        return redisTemplate.opsForValue().get(key(chatId, memberId))?.toObject<Int>() ?: 0
    }

    fun remove(chatId: String, memberId: Long) {
        redisTemplate.delete(key(chatId, memberId))
    }

    private fun key(chatId: String, memberId: Long) = "unread:$chatId:$memberId"
}
