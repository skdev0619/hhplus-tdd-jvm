package io.hhplus.tdd.point.service

import org.junit.jupiter.api.BeforeEach

class PointServiceTest {

    private lateinit var service: PointService

    @BeforeEach
    fun setUp() {
        //service = PointService()
    }
    /*
    @DisplayName("충전할 금액을 0을 입력하면 예외 발생한다")
    @ValueSource(longs = [0])
    @ParameterizedTest
    fun chargeIfZero(amount: Long) {
        assertThatIllegalArgumentException()
            .isThrownBy { service.charge(1L, amount) }
            .withMessage("충전할 포인트는 0보다 커야 합니다")
    }

    @DisplayName("100만 포인트 이상 충전하면 예외 발생한다")
    @ValueSource(longs = [1_000_000, 1_000_001])
    @ParameterizedTest
    fun chargeIfOver(chargeAmount: Long) {
        assertThatIllegalArgumentException()
            .isThrownBy { service.charge(1L, chargeAmount) }
            .withMessage("100만 포인트 이상 충전할 수 없습니다")
    }*/
}
