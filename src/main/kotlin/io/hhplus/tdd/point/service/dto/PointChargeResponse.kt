package io.hhplus.tdd.point.service.dto

import io.hhplus.tdd.point.domain.TransactionType

data class PointChargeResponse(
    val id: Long,
    val transactionType: TransactionType,
    val chargedAmount: Long,
    val newBalance: Long
)