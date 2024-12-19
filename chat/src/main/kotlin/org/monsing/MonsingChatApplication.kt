package org.monsing

import org.monsing.config.RedisProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(RedisProperties::class)
@SpringBootApplication
class MonsingChatApplication

fun main() {
    runApplication<MonsingChatApplication>()
}
