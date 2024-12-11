package org.monsing.domain.session

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@Component
class GlobalSessionStorage(private val redisTemplate: RedisTemplate<Long, String>) {

    companion object {
        private val SESSION_EXPIRE_MINUTES = 15.minutes.toJavaDuration()
    }

    fun saveSession(memberId: Long, serverId: String) {
        redisTemplate.opsForList().rightPush(memberId, serverId)
        redisTemplate.expire(memberId, SESSION_EXPIRE_MINUTES)
    }

    fun getSession(memberId: Long): List<String> {
        return redisTemplate.opsForList()
            .range(memberId, 0, -1)
            .orEmpty()
    }

    fun removeSession(memberId: Long, serverId: String) {
        redisTemplate.opsForList().remove(memberId, 1, serverId)
    }
}
