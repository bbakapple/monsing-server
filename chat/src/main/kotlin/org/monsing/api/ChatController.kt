package org.monsing.api

import org.monsing.auth.Auth
import org.monsing.auth.AuthPayload
import org.monsing.auth.jwt.TokenPayload
import org.monsing.chat.ChatService
import org.monsing.chat.Message
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @Auth
    @PostMapping("/chats")
    fun createChat(
        @RequestBody request: CreateChatRequest,
        @AuthPayload tokenPayload: TokenPayload
    ): ResponseEntity<Unit> {
        chatService.createChat(request.memberId, tokenPayload.id)
        return ResponseEntity.ok().build()
    }

    @Auth
    @GetMapping("/chats/{id}/messages")
    fun getMessages(
        @PathVariable id: String,
        @RequestParam(required = false) lastId: String?,
        @RequestParam(required = false) size: Int?,
        @AuthPayload tokenPayload: TokenPayload
    ): ResponseEntity<List<MessageResponse>> {
        val response = chatService.getMessages(id, lastId, size, tokenPayload.id)
            .map {
                MessageResponse(
                    id = requireNotNull(it.id),
                    senderId = it.senderId,
                    content = it.content,
                    createdAt = it.createdAt
                )
            }

        return ResponseEntity.ok(response)
    }

    @Auth
    @GetMapping("/chats")
    fun getChats(
        @AuthPayload tokenPayload: TokenPayload
    ): ResponseEntity<List<ChatThumbnailResponse>> {
        val response = chatService.findChatByMemberId(tokenPayload.id).map {
            val lastMessage = chatService.findLastMessageByChatId(it.id)
            ChatThumbnailResponse(
                it.id,
                lastMessage?.senderId,
                lastMessage?.content,
                lastMessage?.createdAt
            )
        }

        return ResponseEntity.ok(response)
    }
}
