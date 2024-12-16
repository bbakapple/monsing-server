package org.monsing.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(private val redisProperties: RedisProperties) {

    @Bean
    fun connectionFactory() = LettuceConnectionFactory(redisStandAloneConfiguration())

    @Bean
    fun redisStandAloneConfiguration() = RedisStandaloneConfiguration().apply {
        hostName = redisProperties.host
        port = redisProperties.port
        username = redisProperties.username
        password = RedisPassword.of(redisProperties.password)
    }

    @Bean
    fun redisTemplate() = RedisTemplate<Long, String>().apply {
        connectionFactory = connectionFactory()
        keySerializer = GenericToStringSerializer(Long::class.java)
        valueSerializer = StringRedisSerializer()
    }

    @Bean
    fun ticketRedisTemplate() = RedisTemplate<String, Long>().apply {
        connectionFactory = connectionFactory()
        keySerializer = StringRedisSerializer()
        valueSerializer = GenericToStringSerializer(Long::class.java)
    }
}
