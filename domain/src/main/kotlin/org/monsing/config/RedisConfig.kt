package org.monsing.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@EnableConfigurationProperties(RedisProperties::class)
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

    @Bean
    fun fcmTokenRedisTemplate() = RedisTemplate<String, String>().apply {
        connectionFactory = connectionFactory()
        keySerializer = StringRedisSerializer()
        valueSerializer = StringRedisSerializer()
    }

    @Bean
    fun generalRedisTemplate() = RedisTemplate<String, Any>().apply {
        connectionFactory = connectionFactory()
        keySerializer = StringRedisSerializer()
        valueSerializer = GenericJackson2JsonRedisSerializer(
            ObjectMapper().registerModule(kotlinModule())
        )
    }
}

inline fun <reified T> Any.toObject(): T {
    return ObjectMapper().registerModule(kotlinModule()).run {
        readValue(writeValueAsString(this@toObject), T::class.java)
    }
}
