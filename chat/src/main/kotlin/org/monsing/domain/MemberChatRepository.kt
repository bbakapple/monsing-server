package org.monsing.domain

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component

@Component
class MemberChatRepository(private val mongoTemplate: MongoTemplate) {

    fun save(memberChat: MemberChat) {
        mongoTemplate.save(memberChat, "member_chat")
    }
}
