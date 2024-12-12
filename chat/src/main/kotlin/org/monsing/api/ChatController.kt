package org.monsing.api

import org.monsing.application.ChatService
import org.monsing.domain.Message
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(private val chatService: ChatService) {

    @PostMapping("/relay")
    fun relayMessage(
        @RequestBody message: Message,
        @RequestParam receiverId: Long
    ) {
        chatService.relayMessage(
            receiverId,
            message
        )
    }
}
