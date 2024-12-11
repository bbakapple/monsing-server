package org.monsing.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun connectionFactory() = LettuceConnectionFactory().apply {
        //TODO: 레디스 설정
    }

    @Bean
    fun redisTemplate() = RedisTemplate<Long, String>().apply {
        connectionFactory = connectionFactory()
        keySerializer = GenericToStringSerializer(Long::class.java)
        valueSerializer = StringRedisSerializer()
    }
}
