package org.monsing.record

import io.swagger.v3.oas.annotations.Operation
import org.monsing.auth.Auth
import org.monsing.auth.AuthPayload
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.record.feedback.FeedbackItem
import org.monsing.record.feedback.FeedbackService
import org.monsing.record.response.FeedbackResponse
import org.monsing.util.toNonNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/feedbacks")
class FeedbackController(
    private val feedbackService: FeedbackService
) {
    @Auth
    @PostMapping("/items")
    @Operation(summary = "피드백 상품 생성")
    fun createFeedbackTicket(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @RequestBody request: FeedbackTicketCreateRequest
    ) {
        feedbackService.createFeedbackItem(authTokenPayload.id, request.price, request.description, request.amount)
    }

    @Operation(summary = "피드백 상품 조회")
    @GetMapping("/items/{itemId}")
    fun getFeedbackItem(
        @PathVariable itemId: Long
    ): FeedbackItem {
        return feedbackService.getFeedbackItem(itemId)
    }

    @Operation(summary = "피드백 다건 조회")
    @GetMapping("/items")
    fun getFeedbackItems(): List<FeedbackItem> {
        return feedbackService.getFeedbackItems()
    }

    @Auth
    @Operation(summary = "피드백 티켓 구매")
    @PostMapping("/tickets")
    fun purchaseFeedbackTicket(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @RequestBody request: FeedbackTicketPurchaseRequest
    ) {
        feedbackService.purchaseFeedbackTicket(authTokenPayload.id, request.amount, request.itemId)
    }

    @Auth
    @Operation(summary = "내 피드백 상품 조회")
    @GetMapping("/items/my")
    fun getMyFeedbackItems(
        @AuthPayload authTokenPayload: AuthTokenPayload
    ): List<FeedbackItem> {
        return feedbackService.getFeedbackItemsByMemberId(authTokenPayload.id)
    }

    @Auth
    @Operation(summary = "피드백 요청")
    @PostMapping("/feedbacks/{feedbackTicketId}")
    fun requestFeedback(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @PathVariable feedbackTicketId: Long,
        @RequestBody request: RequestFeedbackRequest
    ) {
        feedbackService.requestFeedback(authTokenPayload.id, request.recordId, feedbackTicketId)
    }

    @Auth
    @Operation(summary = "내 피드백 조회")
    @GetMapping("/feedbacks/my")
    fun listFeedbacks(
        @AuthPayload authTokenPayload: AuthTokenPayload
    ): List<FeedbackResponse> {
        val feedbacks = feedbackService.findFeedbacksByMemberId(authTokenPayload.id)

        return feedbacks.map {
            FeedbackResponse(
                id = requireNotNull(it.id),
                teacher = it.teacher,
                recordId = it.record.id.toNonNull(),
                detail = it.detail,
                createdAt = it.updatedDate
            )
        }
    }
}

data class RequestFeedbackRequest(
    val recordId: Long
)

data class FeedbackTicketPurchaseRequest(
    val amount: Int,
    val itemId: Long
)

data class FeedbackTicketCreateRequest(
    val amount: Int,
    val price: Int,
    val description: String
)
