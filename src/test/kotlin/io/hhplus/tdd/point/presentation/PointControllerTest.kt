package io.hhplus.tdd.point.presentation

import io.hhplus.tdd.point.domain.TransactionType
import io.hhplus.tdd.point.service.PointService
import io.hhplus.tdd.point.service.dto.PointChargeResponse
import io.hhplus.tdd.point.service.dto.PointHistoryResponse
import io.hhplus.tdd.point.service.dto.PointUseResponse
import io.hhplus.tdd.point.service.dto.UserPointResponse
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(PointController::class)
class PointControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var pointService: PointService

    @DisplayName("특정 유저의 포인트를 조회한다")
    @Test
    fun getPoint() {
        val id = 1L
        val response = UserPointResponse(1L, 1_000L, System.currentTimeMillis())

        `when`(pointService.findById(id)).thenReturn(response)

        mockMvc.perform(get("/point/${id}"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", `is`(response.id.toInt())))
            .andExpect(jsonPath("$.point", `is`(response.point.toInt())))
            .andExpect(jsonPath("$.updateMillis", `is`(response.updateMillis)))

        verify(pointService).findById(id)
    }

    @DisplayName("특정 유저의 포인트 사용/충전 기록을 확인한다")
    @Test
    fun getPointHistory() {
        val id = 1L
        val response = listOf(
            PointHistoryResponse(
                1L,
                1L,
                TransactionType.CHARGE,
                100L,
                System.currentTimeMillis()
            )
        )

        `when`(pointService.getUserPointHistory(id)).thenReturn(response)

        mockMvc.perform(get("/point/${id}/histories"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id", `is`(response.first().id.toInt())))
            .andExpect(jsonPath("$[0].userId", `is`(response.first().userId.toInt())))
            .andExpect(jsonPath("$[0].type", `is`(response.first().type.toString())))
            .andExpect(jsonPath("$[0].amount", `is`(response.first().amount.toInt())))
            .andExpect(jsonPath("$[0].timeMillis", `is`(response.first().timeMillis)))

        verify(pointService).getUserPointHistory(id)
    }

    @DisplayName("특정 유저의 포인트를 충전한다")
    @Test
    fun charge() {
        val id = 1L
        val chargeAmount = 100L
        val response = PointChargeResponse(1L, TransactionType.CHARGE, 100L, 1_000L)

        `when`(pointService.charge(id, 100L)).thenReturn(response)

        mockMvc.perform(
            patch("/point/${id}/charge")
                .content(chargeAmount.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(response.id.toInt())))
            .andExpect(jsonPath("$.transactionType", `is`(response.transactionType.toString())))
            .andExpect(jsonPath("$.chargedAmount", `is`(response.chargedAmount.toInt())))
            .andExpect(jsonPath("$.newBalance", `is`(response.newBalance.toInt())))

        verify(pointService).charge(id, chargeAmount)
    }

    @DisplayName("특정 유저가 포인트를 충전할 때, 최대 값인 100만을 초과하여 예외가 발생한다")
    @ValueSource(longs = [1_000_000])
    @ParameterizedTest
    fun maxCharge(maxAmount: Long) {
        val id = 1L

        `when`(pointService.charge(id, maxAmount))
            .thenThrow(IllegalStateException("포인트는 0이상 1000000이하여야 합니다"))

        mockMvc.perform(
            patch("/point/${id}/charge")
                .content(maxAmount.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.message", `is`("포인트는 0이상 1000000이하여야 합니다")))

        verify(pointService).charge(id, maxAmount)
    }


    @DisplayName("특정 유저가 포인트를 사용한다")
    @Test
    fun use() {
        val id = 1L
        val useAmount = 1_000L
        val response = PointUseResponse(1L, TransactionType.USE, 1_000L, 9_000L)

        `when`(pointService.use(id, useAmount)).thenReturn(response)

        mockMvc.perform(
            patch("/point/${id}/use")
                .content(useAmount.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", `is`(response.id.toInt())))
            .andExpect(jsonPath("$.transactionType", `is`(response.transactionType.toString())))
            .andExpect(jsonPath("$.usedAmount", `is`(response.usedAmount.toInt())))
            .andExpect(jsonPath("$.newBalance", `is`(response.newBalance.toInt())))

        verify(pointService).use(id, useAmount)
    }

    @DisplayName("특정 유저가 포인트를 사용할 때 잔고가 부족해 예외가 발생한다.")
    @Test
    fun InsufficientPoint() {
        val id = 1L
        val useAmount = 1_000L

        `when`(pointService.use(id, useAmount))
            .thenThrow(IllegalStateException("잔액이 부족합니다"))

        mockMvc.perform(
            patch("/point/${id}/use")
                .content(useAmount.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.message", `is`("잔액이 부족합니다")))

        verify(pointService).use(id, useAmount)
    }
}
