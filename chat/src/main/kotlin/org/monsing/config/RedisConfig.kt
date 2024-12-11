package org.monsing.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    fun connectionFactory() = LettuceConnectionFactory().apply {
        //TODO: 레디스 설정
    }

    fun redisTemplate() = RedisTemplate<String, String>().apply {
        connectionFactory = connectionFactory()
        keySerializer = StringRedisSerializer()
        valueSerializer = StringRedisSerializer()
    }
}
