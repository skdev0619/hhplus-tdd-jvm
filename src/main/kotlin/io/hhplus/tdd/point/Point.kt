package io.hhplus.tdd.point

data class Point(val value: Long) {

    companion object {
        const val MIN_VALUE = 0L
        const val MAX_VALUE = 1_000_000L

        val ZERO = Point(MIN_VALUE)
    }

    init {
        validate(value)
    }

    private fun validate(value: Long) {
        if (MIN_VALUE > value || MAX_VALUE < value) {
            throw IllegalArgumentException("포인트는 ${MIN_VALUE}이상 ${MAX_VALUE}이하여야 합니다")
        }
    }

    fun plus(other: Long): Point {
        return Point(this.value + other)
    }

    fun plus(other: Point): Point {
        return other.plus(this.value)
    }

    fun minus(other: Long): Point {
        return Point(this.value - other)
    }

    fun minus(other: Point): Point {
        return Point(this.value - other.value)
    }
}
