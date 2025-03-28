package io.hhplus.tdd.point.service

import io.hhplus.tdd.point.domain.TransactionType
import io.hhplus.tdd.point.fake.FakeLockManager
import io.hhplus.tdd.point.fake.FakePointHistoryRepository
import io.hhplus.tdd.point.fake.FakeUserPointRepository
import io.hhplus.tdd.point.service.dto.PointHistoryResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class PointServiceTest {

    val userPointRepository = FakeUserPointRepository()
    val pointHistoryRepository = FakePointHistoryRepository()
    val lockManager = FakeLockManager()
    val pointService = PointService(userPointRepository, pointHistoryRepository, lockManager)

    @DisplayName("유저가 포인트를 충전하면, 포인트 잔액이 증가하고 포인트 사용 이력이 쌓인다")
    @Test
    fun charge() {
        userPointRepository.insertOrUpdate(1L, 100L)

        val response = pointService.charge(1L, 900L)

        assertAll(
            { assertThat(response.id).isEqualTo(1L) },
            { assertThat(response.chargedAmount).isEqualTo(900L) },
            { assertThat(response.newBalance).isEqualTo(1_000L) },
            { assertThat(response.transactionType).isEqualTo(TransactionType.CHARGE) }
        )
    }

    @DisplayName("유저가 포인트를 사용하면 포인트가 차감되고 포인트 사용 이력이 쌓인다")
    @Test
    fun use() {
        userPointRepository.insertOrUpdate(1L, 10_000L)

        val response = pointService.use(1L, 2_000L)

        assertAll(
            { assertThat(response.id).isEqualTo(1L) },
            { assertThat(response.usedAmount).isEqualTo(2_000L) },
            { assertThat(response.newBalance).isEqualTo(8_000L) },
            { assertThat(response.transactionType).isEqualTo(TransactionType.USE) }
        )
    }

    @DisplayName("id로 특정 유저의 포인트를 조회한다")
    @Test
    fun findById() {
        userPointRepository.insertOrUpdate(1L, 50_000L)

        val response = pointService.findById(1L)

        assertAll(
            { assertThat(response.id).isEqualTo(1L) },
            { assertThat(response.point).isEqualTo(50_000L) },
        )
    }

    @DisplayName("특정 유저의 포인트 사용 이력을 조회한다")
    @Test
    fun getPointHistoryById() {
        userPointRepository.insertOrUpdate(1L, 0L)
        pointService.charge(1L, 1_000L)
        pointService.use(1L, 200L)

        val histories = pointService.getUserPointHistory(1L)

        assertAll(
            { assertThat(histories).hasSize(2) },
            {
                assertThat(histories)
                    .extracting(
                        PointHistoryResponse::id,
                        PointHistoryResponse::userId,
                        PointHistoryResponse::type,
                        PointHistoryResponse::amount
                    ).containsExactly(
                        Tuple.tuple(1L, 1L, TransactionType.CHARGE, 1_000L),
                        Tuple.tuple(2L, 1L, TransactionType.USE, 200L)
                    )
            }
        )
    }
}
