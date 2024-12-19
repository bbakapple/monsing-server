package org.monsing.chat.session

import java.util.concurrent.ConcurrentSkipListMap
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class LocalSessionStorage(
    private val storage: ConcurrentSkipListMap<String, WebSocketSession> = ConcurrentSkipListMap(),
) {

    fun saveSession(memberId: Long, deviceId: String, session: WebSocketSession) {
        storage[createKey(memberId, deviceId)] = session
    }

    fun getSessionByMemberId(memberId: Long): Set<WebSocketSession>? {
        return storage.subMap(memberId.convert(), (memberId + 1).convert()).values.toSet()
    }

    fun removeSession(memberId: Long, deviceId: String, session: WebSocketSession) {
        storage.remove(createKey(memberId, deviceId))
    }

    private fun createKey(memberId: Long, deviceId: String): String {
        return "${memberId.convert()}$deviceId"
    }

    private fun Long.convert(): String {
        return this.toString().padStart(20, '0')
    }
}
