package io.hhplus.tdd.point

data class UserPoint(
    val id: Long,
    val point: Point,
    val updateMillis: Long,
){
    fun charge(id: Long, amount: Long): UserPoint {
        if (amount == 0L) {
            throw IllegalArgumentException("충전할 포인트는 0보다 커야 합니다")
        }

        val newPoint = point.plus(amount)
        return UserPoint(id, newPoint, System.currentTimeMillis())
    }
}
