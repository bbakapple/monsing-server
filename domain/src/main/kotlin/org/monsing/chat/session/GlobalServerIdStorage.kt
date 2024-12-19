package org.monsing.chat.session

import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class GlobalServerIdStorage(private val redisTemplate: RedisTemplate<Long, String>) {

    fun saveServerId(memberId: Long, serverId: String) {
        redisTemplate.opsForSet().add(memberId, serverId)
        redisTemplate.expire(memberId, SESSION_EXPIRE_MINUTES)
    }

    fun getServerId(memberId: Long): Set<String>? {
        return redisTemplate.opsForSet()
            .members(memberId)
    }

    fun removeServerId(memberId: Long, serverId: String) {
        redisTemplate.opsForSet().remove(memberId, serverId)
    }

    companion object {
        private val SESSION_EXPIRE_MINUTES = 15.minutes.toJavaDuration()
    }
}
