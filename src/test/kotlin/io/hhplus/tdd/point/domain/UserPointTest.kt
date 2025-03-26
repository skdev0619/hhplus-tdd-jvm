package io.hhplus.tdd.point.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserPointTest {

    @DisplayName("충전할 금액을 0을 입력하면 예외 발생한다")
    @ValueSource(longs = [0])
    @ParameterizedTest
    fun chargeIfZero(amount: Long) {
        val userPoint = createUserPoint(1L, 0L)

        assertThatIllegalArgumentException()
            .isThrownBy { userPoint.charge(1L, amount) }
            .withMessage("충전할 포인트는 0보다 커야 합니다")
    }

    @DisplayName("유저의 포인트를 충전한다")
    @Test
    fun charge() {
        val userPoint = createUserPoint(1L, 100L)

        val result = userPoint.charge(1L, 500L)

        assertThat(result.point).isEqualTo(Point(600L))
    }

    @DisplayName("유저의 포인트 잔액보다 큰 금액을 사용하면 예외 발생한다")
    @Test
    fun useIfOver() {
        val userPoint = createUserPoint(1L, 100L)

        assertThatIllegalStateException()
            .isThrownBy { userPoint.use(1L, 101L) }
            .withMessage("잔액이 부족합니다")
    }

    private fun createUserPoint(id: Long, amount: Long): UserPoint {
        return UserPoint(id, Point(amount), System.currentTimeMillis())
    }
}