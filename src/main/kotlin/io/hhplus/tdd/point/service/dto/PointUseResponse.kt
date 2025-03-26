package io.hhplus.tdd.point.service.dto

import io.hhplus.tdd.point.domain.TransactionType

data class PointUseResponse(
    val id: Long,
    val transactionType: TransactionType,
    val usedAmount: Long,
    val newBalance: Long
)