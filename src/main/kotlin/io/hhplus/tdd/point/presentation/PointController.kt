package io.hhplus.tdd.point.presentation

import io.hhplus.tdd.point.service.PointService
import io.hhplus.tdd.point.service.dto.PointChargeResponse
import io.hhplus.tdd.point.service.dto.PointHistoryResponse
import io.hhplus.tdd.point.service.dto.PointUseResponse
import io.hhplus.tdd.point.service.dto.UserPointResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
class PointController(
    private val pointService: PointService
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    fun point(
        @PathVariable id: Long,
    ): ResponseEntity<UserPointResponse> {
        return ResponseEntity.ok(pointService.findById(id))
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    fun history(
        @PathVariable id: Long,
    ): ResponseEntity<List<PointHistoryResponse>> {
        return ResponseEntity.ok(pointService.getUserPointHistory(id))
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    fun charge(
        @PathVariable id: Long,
        @RequestBody amount: Long,
    ): ResponseEntity<PointChargeResponse> {
        return ResponseEntity.ok(pointService.charge(id, amount))
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    fun use(
        @PathVariable id: Long,
        @RequestBody amount: Long,
    ): ResponseEntity<PointUseResponse> {
        return ResponseEntity.ok(pointService.use(id, amount))
    }
}
