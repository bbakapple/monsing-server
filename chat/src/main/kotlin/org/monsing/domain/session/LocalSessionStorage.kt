package org.monsing.domain.session

import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class LocalSessionStorage(
    private val storage: MutableMap<Long, Set<WebSocketSession>> = mutableMapOf()
) {

    fun saveSession(memberId: Long, session: WebSocketSession) {
        storage[memberId] = storage[memberId]?.plus(session) ?: setOf(session)
    }

    fun getSession(memberId: Long): Set<WebSocketSession>? {
        return storage[memberId]
    }

    fun removeSession(memberId: Long, session: WebSocketSession) {
        storage[memberId] = storage[memberId]?.minus(session) ?: emptySet()
    }
}
