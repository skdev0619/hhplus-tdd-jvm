package io.hhplus.tdd.point.service

import io.hhplus.tdd.point.domain.TransactionType
import io.hhplus.tdd.point.repository.PointHistoryRepository
import io.hhplus.tdd.point.repository.UserPointRepository
import io.hhplus.tdd.point.service.dto.PointChargeResponse
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userPointRepository: UserPointRepository,
    private val pointHistoryRepository: PointHistoryRepository
) {
    fun charge(id: Long, amount: Long): PointChargeResponse {
        val userPoint = userPointRepository.selectById(id)
        val resultUserPoint = userPoint.charge(id, amount)

        userPointRepository.insertOrUpdate(id = id, amount = resultUserPoint.point.value)
        pointHistoryRepository.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis())
        return PointChargeResponse(id, TransactionType.CHARGE, amount, resultUserPoint.point.value)
    }
}
