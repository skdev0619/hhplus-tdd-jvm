package io.hhplus.tdd.point.fake

import io.hhplus.tdd.point.domain.Point
import io.hhplus.tdd.point.domain.UserPoint
import io.hhplus.tdd.point.repository.UserPointRepository

class FakeUserPointRepository : UserPointRepository {
    private val userPoints = mutableMapOf<Long, UserPoint>()

    override fun selectById(id: Long): UserPoint {
        return userPoints[id] ?: UserPoint(id = id, point = Point.ZERO, updateMillis = System.currentTimeMillis())
    }

    override fun insertOrUpdate(
        id: Long,
        amount: Long,
    ): UserPoint {
        val userPoint = UserPoint(id = id, point = Point(amount), updateMillis = System.currentTimeMillis())
        userPoints[id] = userPoint
        return userPoint
    }
}
