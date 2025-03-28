package io.hhplus.tdd.point.service

import io.hhplus.tdd.point.domain.TransactionType
import io.hhplus.tdd.point.repository.PointHistoryRepository
import io.hhplus.tdd.point.repository.UserPointRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SpringBootTest
class PointServiceConcurrencyTest
@Autowired
constructor(
    private val userPointRepository: UserPointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
    private val pointService: PointService
) {

    private val threadCount = 3
    private lateinit var executorService: ExecutorService
    private lateinit var latch: CountDownLatch

    @BeforeEach
    fun setUp() {
        executorService = Executors.newFixedThreadPool(threadCount)
        latch = CountDownLatch(threadCount)
    }

    @DisplayName("포인트 충전")
    @Nested
    inner class Charge {
        @DisplayName("두 명의 유저가 만 포인트를 동시에 3번 충전 요청하면, 각 유저의 포인트 잔액은 3만 포인트가 되고 충전 이력이 3건 기록된다")
        @ValueSource(longs = [10_000])
        @ParameterizedTest
        fun charge(chargeAmount: Long) {
            //초기값 0원
            userPointRepository.insertOrUpdate(1L, 0)
            userPointRepository.insertOrUpdate(2L, 0)

            repeat(threadCount) {
                executorService.execute {
                    try {
                        pointService.charge(1L, chargeAmount)
                        pointService.charge(2L, chargeAmount)
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()
            executorService.shutdown()

            val resultPoint1 = userPointRepository.selectById(1L)
            val resultHistory1 = pointHistoryRepository.selectAllByUserId(1L)
            val resultPoint2 = userPointRepository.selectById(2L)
            val resultHistory2 = pointHistoryRepository.selectAllByUserId(2L)

            assertThat(resultPoint1.point.value).isEqualTo(30_000L)
            assertThat(resultHistory1.filter { it.type == TransactionType.CHARGE }).hasSize(3)
            assertThat(resultPoint2.point.value).isEqualTo(30_000L)
            assertThat(resultHistory2.filter { it.type == TransactionType.CHARGE }).hasSize(3)
        }

        @DisplayName("포인트 최대 한도가 100만이므로, 두명의 유저가 50만 포인트를 동시에 3번 충전 요청하면, 각각 100만 포인트가 되고 충전 이력이 2건 기록된다")
        @ValueSource(longs = [500_000L])
        @ParameterizedTest
        fun chargeException(chargeAmount: Long) {
            //given
            //초기값 0원
            userPointRepository.insertOrUpdate(3L, 0)
            userPointRepository.insertOrUpdate(4L, 0)

            //when
            repeat(threadCount) {
                executorService.execute {
                    try {
                        pointService.charge(3L, chargeAmount)
                        pointService.charge(4L, chargeAmount)
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()
            executorService.shutdown()

            //then
            val resultPoint1 = userPointRepository.selectById(3L)
            val resultHistory1 = pointHistoryRepository.selectAllByUserId(3L)
            val resultPoint2 = userPointRepository.selectById(4L)
            val resultHistory2 = pointHistoryRepository.selectAllByUserId(4L)

            assertThat(resultPoint1.point.value).isEqualTo(100_0000L)
            assertThat(resultHistory1.filter { it.type == TransactionType.CHARGE }).hasSize(2)
            assertThat(resultPoint2.point.value).isEqualTo(100_0000L)
            assertThat(resultHistory2.filter { it.type == TransactionType.CHARGE }).hasSize(2)
        }
    }

    @DisplayName("포인트 사용")
    @Nested
    inner class Use {
        @DisplayName("두 유저의 포인트 잔액이 5만일 때, 각 유저가 만 포인트를 동시에 3번 사용하면, 각각 2만 포인트가 되고 사용 이력이 3건 기록된다")
        @ValueSource(longs = [10_000])
        @ParameterizedTest
        fun use(useAmount: Long) {
            //given
            //초기 잔액
            userPointRepository.insertOrUpdate(5L, 50_000L)
            userPointRepository.insertOrUpdate(6L, 50_000L)

            //when
            repeat(threadCount) { i ->
                executorService.execute {
                    try {
                        pointService.use(5L, useAmount)
                        pointService.use(6L, useAmount)
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()
            executorService.shutdown()

            //then
            val resultPoint1 = userPointRepository.selectById(5L)
            val resultHistory1 = pointHistoryRepository.selectAllByUserId(5L)
            val resultPoint2 = userPointRepository.selectById(6L)
            val resultHistory2 = pointHistoryRepository.selectAllByUserId(6L)

            assertThat(resultPoint1.point.value).isEqualTo(20_000L)
            assertThat(resultHistory1.filter { it.type == TransactionType.USE }).hasSize(3)
            assertThat(resultPoint2.point.value).isEqualTo(20_000L)
            assertThat(resultHistory2.filter { it.type == TransactionType.USE }).hasSize(3)
        }

        @DisplayName("포인트 잔액이 2만일 때, 만 포인트를 동시에 3번 사용하면, 최종 포인트가 0포인트가 되고 사용 이력이 2건 기록된다")
        @ValueSource(longs = [10_000])
        @ParameterizedTest
        fun useException(useAmount: Long) {
            //given
            //초기 잔액
            userPointRepository.insertOrUpdate(7L, 20_000L)
            userPointRepository.insertOrUpdate(8L, 20_000L)

            //when
            repeat(threadCount) { i ->
                executorService.execute {
                    try {
                        pointService.use(7L, useAmount)
                        pointService.use(8L, useAmount)
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()
            executorService.shutdown()

            //then
            val resultPoint1 = userPointRepository.selectById(7L)
            val resultHistory1 = pointHistoryRepository.selectAllByUserId(7L)
            val resultPoint2 = userPointRepository.selectById(8L)
            val resultHistory2 = pointHistoryRepository.selectAllByUserId(8L)

            assertThat(resultPoint1.point.value).isZero()
            assertThat(resultHistory1.filter { it.type == TransactionType.USE }).hasSize(2)
            assertThat(resultPoint2.point.value).isZero()
            assertThat(resultHistory2.filter { it.type == TransactionType.USE }).hasSize(2)
        }
    }

    @DisplayName("포인트 충전 + 사용")
    @Nested
    inner class ChargeUse {

        @DisplayName("두 유저가 만 포인트를 동시에 3번 충전, 3번 사용하면 각각 포인트는 0원이 되고, 충전 이력 3건, 사용이력 3건이 기록된다")
        @ValueSource(longs = [10_000])
        @ParameterizedTest
        fun chargeUse(amount: Long) {
            //given
            //초기 잔액
            userPointRepository.insertOrUpdate(9L, 0L)
            userPointRepository.insertOrUpdate(10L, 0L)

            //when
            repeat(threadCount) { i ->
                executorService.execute {
                    try {
                        pointService.charge(9L, amount)
                        pointService.use(9L, amount)

                        pointService.charge(10L, amount)
                        pointService.use(10L, amount)
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()
            executorService.shutdown()

            //then
            val resultPoint1 = userPointRepository.selectById(9L)
            val resultHistory1 = pointHistoryRepository.selectAllByUserId(9L)
            val resultPoint2 = userPointRepository.selectById(10L)
            val resultHistory2 = pointHistoryRepository.selectAllByUserId(10L)

            assertThat(resultPoint1.point.value).isZero()
            assertThat(resultHistory1.filter { it.type == TransactionType.CHARGE }).hasSize(3)
            assertThat(resultHistory1.filter { it.type == TransactionType.USE }).hasSize(3)

            assertThat(resultPoint2.point.value).isZero()
            assertThat(resultHistory2.filter { it.type == TransactionType.CHARGE }).hasSize(3)
            assertThat(resultHistory2.filter { it.type == TransactionType.USE }).hasSize(3)
        }

    }
}
