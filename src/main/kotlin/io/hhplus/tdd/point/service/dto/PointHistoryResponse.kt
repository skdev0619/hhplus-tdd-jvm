package io.hhplus.tdd.point.service.dto

import io.hhplus.tdd.point.domain.TransactionType

data class PointHistoryResponse (
    val id: Long,
    val userId: Long,
    val type: TransactionType,
    val amount: Long,
    val timeMillis: Long
)