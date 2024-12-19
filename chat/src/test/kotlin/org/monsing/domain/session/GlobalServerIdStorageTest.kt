package org.monsing.domain.session

import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
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

    @Test
    fun `동시성 보장 테스트`() {
        val memberId = 1L
        val count = 5

        val latch = CountDownLatch(count)
        val pool = Executors.newFixedThreadPool(count)

        for (i in 1..count) {
            pool.execute {
                globalServerIdStorage.saveServerId(memberId, "server-id-$i")
                latch.countDown()
            }
        }
        latch.await()

        val ids = globalServerIdStorage.getServerId(memberId)

        ids?.size shouldBe count
    }
}
