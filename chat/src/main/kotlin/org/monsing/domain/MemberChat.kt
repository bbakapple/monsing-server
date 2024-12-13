package org.monsing.domain

import java.util.UUID

class MemberChat(
    var id: String? = UUID.randomUUID().toString(),
    val memberId: Long,
    val chatId: String
)
