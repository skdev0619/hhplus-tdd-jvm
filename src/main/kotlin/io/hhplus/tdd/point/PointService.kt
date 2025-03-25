package io.hhplus.tdd.point

class PointService(

) {
    fun charge(id: Long, amount: Long) {
        if (amount <= 0) {
            throw IllegalArgumentException("양수인 정수만 충전 가능합니다")
        }
    }
}
