package org.monsing.domain.session

import io.kotest.matchers.collections.shouldContainOnly
import org.junit.jupiter.api.Test
import org.monsing.chat.session.GlobalServerIdStorage
import org.monsing.support.SpringBootTestWithRedis
import org.springframework.beans.factory.annotation.Autowired

class GlobalServerIdStorageTest(
    @Autowired private val globalServerIdStorage: GlobalServerIdStorage
) : SpringBootTestWithRedis() {

    @Test
    fun `서버 아이디 저장 및 조회`() {
        globalServerIdStorage.saveServerId(1, "server-id-1")

        val ids = globalServerIdStorage.getServerId(1)

        ids shouldContainOnly setOf("server-id-1")
    }

    @Test
    fun `서버 아이디 저장 및 삭제`() {
        globalServerIdStorage.saveServerId(1, "server-id-1")
        globalServerIdStorage.saveServerId(1, "server-id-2")

        globalServerIdStorage.removeServerId(1, "server-id-1")

        val ids = globalServerIdStorage.getServerId(1)

        ids shouldContainOnly setOf("server-id-2")
    }
}
