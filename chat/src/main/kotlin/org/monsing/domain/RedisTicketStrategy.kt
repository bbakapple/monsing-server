package org.monsing.domain

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisTicketStrategy(private val ticketRedisTemplate: RedisTemplate<String, Long>) : MessageIdStrategy {

    override fun generateId(message: Message) {
        ticketRedisTemplate.opsForValue().get(TICKET_KEY)
            ?.run {
                ticketRedisTemplate.opsForValue().set(
                    TICKET_KEY,
                    0,
                    Duration.ofMinutes(1)
                )
            }

        val additionalId = requireNotNull(ticketRedisTemplate.opsForValue().increment(TICKET_KEY)) {
            "Failed to generate id"
        }

        val dataTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSS"))

        val id = "$dataTime${additionalId.toString().padStart(6, '0')}"

        message.id = id
    }

    companion object {
        private const val TICKET_KEY = "message_id_ticket"
    }
}
