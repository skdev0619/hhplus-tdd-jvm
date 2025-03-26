package io.hhplus.tdd.point

class PointService(

) {
    fun charge(id: Long, amount: Long) {
        if (amount == 0L) {
            throw IllegalArgumentException("충전할 포인트는 0보다 커야 합니다")
        }
        /*
        if (amount >= 1_000_000) {
            throw IllegalArgumentException("100만 포인트 이상 충전할 수 없습니다")
        }*/
    }
}
