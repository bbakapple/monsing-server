package org.monsing.chat.session

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.junit.jupiter.api.Test

class LocalSessionStorageTest {

    private val localSessionStorage = LocalSessionStorage()

    @Test
    fun `동시성 테스트`() {
        val memberId = 1L
        val count = 5

        val latch = CountDownLatch(count)
        val pool = Executors.newFixedThreadPool(count)

        for (i in 1..count) {
            pool.execute {
                localSessionStorage.saveSession(memberId, mockk())
                latch.countDown()
            }
        }
        latch.await()

        val sessions = localSessionStorage.getSession(memberId)

        sessions?.size shouldBe count
    }
}
