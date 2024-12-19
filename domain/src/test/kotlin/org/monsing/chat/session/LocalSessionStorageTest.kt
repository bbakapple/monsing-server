package org.monsing.chat.session

import io.kotest.matchers.shouldBe
import io.mockk.mockk
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.junit.jupiter.api.Test

class LocalSessionStorageTest {

    private val localSessionStorage = LocalSessionStorage()

    @Test
    fun `prefix로 세션을 가져온다`() {
        localSessionStorage.saveSession(1, "asdsda", mockk())
        localSessionStorage.saveSession(1, "meannot", mockk())
        localSessionStorage.saveSession(2, "deveve", mockk())
        localSessionStorage.saveSession(3, "devvivi", mockk())

        val sessions = localSessionStorage.getSessionByMemberId(1)

        sessions?.size shouldBe 2
    }

    @Test
    fun `prefix 자리수가 다른 경우에도 정상적으로 세션을 가져온다`() {
        localSessionStorage.saveSession(10, "1asd", mockk())
        localSessionStorage.saveSession(100, "0ag1", mockk())
        localSessionStorage.saveSession(1, "0asg01", mockk())
        localSessionStorage.saveSession(1, "0akbak001", mockk())

        val sessions = localSessionStorage.getSessionByMemberId(1)

        sessions?.size shouldBe 2
    }

    @Test
    fun `결과값이 없을 때 빈 set을 반환한다`() {
        val sessions = localSessionStorage.getSessionByMemberId(1)

        sessions?.size shouldBe 0
    }

    @Test
    fun `동시성 테스트`() {
        val memberId = 1L
        val count = 5

        val latch = CountDownLatch(count)
        val pool = Executors.newFixedThreadPool(count)

        for (i in 1..count) {
            pool.execute {
                localSessionStorage.saveSession(memberId, "$i", mockk())
                latch.countDown()
            }
        }
        latch.await()

        val sessions = localSessionStorage.getSessionByMemberId(memberId)

        sessions?.size shouldBe count
    }

    @Test
    fun `session key에 따라 정상적으로 session이 삭제된다`() {
        localSessionStorage.saveSession(1, "1", mockk())
        localSessionStorage.saveSession(1, "2", mockk())
        localSessionStorage.saveSession(1, "3", mockk())
        localSessionStorage.saveSession(1, "4", mockk())

        localSessionStorage.removeSession(1, "1")
        localSessionStorage.removeSession(1, "4")

        val sessions = localSessionStorage.getSessionByMemberId(1)

        sessions?.size shouldBe 2
    }
}
