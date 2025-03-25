package io.hhplus.tdd.point

import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PointServiceTest {

    private lateinit var service: PointService

    @BeforeEach
    fun setUp() {
        service = PointService()
    }

    @DisplayName("0이하의 포인트를 충전하면 예외 발생한다")
    @ValueSource(longs = [-1, 0])
    @ParameterizedTest
    fun chargeIfBelowZero(amount: Long) {
        assertThatIllegalArgumentException()
            .isThrownBy { service.charge(1L, amount) }
            .withMessage("양수인 정수만 충전 가능합니다")
    }
}
