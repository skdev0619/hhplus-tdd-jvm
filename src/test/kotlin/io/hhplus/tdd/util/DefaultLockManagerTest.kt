package io.hhplus.tdd.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class DefaultLockManagerTest {

    @DisplayName("숫자 0을 동시에 5씩 3번 증가시키면, 락이 적용되어 최종 결과는 15이 된다")
    @Test
    fun executeWithLock() {
        val threadCount = 3
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        var number = 0
        val lockManager = DefaultLockManager()
        repeat(threadCount) {
            executorService.submit {
                lockManager.executeWithLock(1L) {
                    try {
                        Thread.sleep(100)
                        number += 5
                    } finally {
                        latch.countDown()
                    }
                }
            }
        }

        latch.await()
        executorService.shutdownNow()

        assertThat(number).isEqualTo(15)
    }
}
