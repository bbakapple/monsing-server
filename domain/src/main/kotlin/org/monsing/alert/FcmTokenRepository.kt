package org.monsing.alert

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository


@Repository
class FcmTokenRepository(
    private val fcmTokenRedisTemplate: RedisTemplate<String, String>
) {

    fun saveToken(memberId: Long, token: String) {
        fcmTokenRedisTemplate.opsForSet().add("$TOKEN_PREFIX$memberId", token)
    }

    fun deleteToken(memberId: Long, token: String) {
        fcmTokenRedisTemplate.opsForSet().remove("$TOKEN_PREFIX$memberId", token)
    }

    fun findToken(memberId: Long): Set<String> {
        return fcmTokenRedisTemplate.opsForSet().members("$TOKEN_PREFIX$memberId") ?: emptySet()
    }

    companion object {
        private const val TOKEN_PREFIX = "TOKEN:"
    }
}
