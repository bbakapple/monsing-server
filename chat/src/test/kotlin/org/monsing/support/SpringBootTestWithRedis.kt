package org.monsing.support

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest(classes = [TestRedisConfig::class])
abstract class SpringBootTestWithRedis {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<Long, String>

    @BeforeEach
    fun clearRedis() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushDb()
    }
}
