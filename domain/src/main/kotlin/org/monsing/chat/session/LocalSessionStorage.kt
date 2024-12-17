package org.monsing.chat.session

import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class LocalSessionStorage(
    private val storage: HashMap<Long, Set<WebSocketSession>> = HashMap(),
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
