package org.monsing.support

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
class TestRedisConfig {

    private val redisServer = RedisServer()

    @PostConstruct
    fun init() {
        redisServer.start()
    }

    @PreDestroy
    fun destroy() {
        redisServer.stop()
    }
}
