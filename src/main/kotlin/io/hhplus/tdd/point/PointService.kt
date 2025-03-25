package io.hhplus.tdd.point

class PointService(

) {
    fun charge(id: Long, amount: Long) {
        if (amount <= 0) {
            throw IllegalArgumentException("양수인 정수만 충전 가능합니다")
        }

        if (amount >= 1_000_000) {
            throw IllegalArgumentException("100만 포인트 이상 충전할 수 없습니다")
        }
    }
}
