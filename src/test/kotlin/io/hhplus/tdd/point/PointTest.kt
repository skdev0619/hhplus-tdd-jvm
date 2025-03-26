package io.hhplus.tdd.point

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PointTest {

    @DisplayName("포인트는 0이상 100만 이하의 값이 들어오지 않으면 예외 발생한다")
    @ValueSource(longs = [-1, 1_000_001])
    @ParameterizedTest
    fun validateRange(value: Long) {
        assertThatIllegalArgumentException()
            .isThrownBy { Point(value) }
            .withMessage("포인트는 0이상 1000000이하여야 합니다")
    }

    @DisplayName("포인트끼리 더한다")
    @Test
    fun plus() {
        val point = Point(100L)

        val result = point.plus(200L);

        assertThat(result).isEqualTo(Point(300L))
    }

    @DisplayName("포인트를 차감한다")
    @Test
    fun minus() {
        val point = Point(1_000L)

        val result = point.minus(200L)

        assertThat(result).isEqualTo(Point(800L))
    }
}
