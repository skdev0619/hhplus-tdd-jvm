package io.hhplus.tdd.point.service.dto

import io.hhplus.tdd.point.domain.UserPoint

data class UserPointResponse(
    val id: Long,
    val point: Long,
    val updateMillis: Long
) {
    companion object {
        fun from(userPoint: UserPoint): UserPointResponse {
            return UserPointResponse(
                userPoint.id,
                userPoint.point.value,
                userPoint.updateMillis
            )
        }
    }
}