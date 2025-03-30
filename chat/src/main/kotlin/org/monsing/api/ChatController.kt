package org.monsing.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.monsing.auth.Auth
import org.monsing.auth.AuthPayload
import org.monsing.auth.jwt.AuthTokenPayload
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
        @AuthPayload authTokenPayload: AuthTokenPayload
    ): ResponseEntity<ChatCreatedResponse> {
        val id = chatService.createChat(request.memberId, authTokenPayload.id)
        return ResponseEntity.ok(ChatCreatedResponse(id))
    }

    @Auth
    @GetMapping("/chats/{id}/messages")
    fun getMessages(
        @PathVariable id: String,
        @RequestParam(required = false) lastId: String?,
        @RequestParam(required = false) size: Int?,
        @AuthPayload authTokenPayload: AuthTokenPayload
    ): ResponseEntity<List<MessageResponse>> {
        val response = chatService.getMessages(id, lastId, size, authTokenPayload.id)
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
        @AuthPayload authTokenPayload: AuthTokenPayload
    ): ResponseEntity<List<ChatThumbnailResponse>> {
        val response = chatService.findChatByMemberId(authTokenPayload.id).map {
            val thumbnail = chatService.findChatThumbnail(it.id, authTokenPayload.id)
            ChatThumbnailResponse(
                it.id,
                thumbnail.opponentId,
                thumbnail.message?.senderId,
                thumbnail.message?.content,
                thumbnail.message?.createdAt
            )
        }

        return ResponseEntity.ok(response)
    }
}

data class ChatCreatedResponse @JsonCreator constructor(
    @JsonProperty("id")
    val id: String
)
