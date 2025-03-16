package org.monsing.record

import io.swagger.v3.oas.annotations.Operation
import org.monsing.auth.Auth
import org.monsing.auth.AuthPayload
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.member.teacher.ExpertiseType
import org.monsing.member.teacher.GenderType
import org.monsing.record.feedback.FeedbackItem
import org.monsing.record.feedback.FeedbackService
import org.monsing.record.response.FeedbackResponse
import org.monsing.util.toNonNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/feedbacks")
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
    ): FeedbackItemResponse {
        val feedbackItem = feedbackService.getFeedbackItem(itemId)

        return feedbackItem.toResponse()
    }

    @Operation(summary = "피드백 다건 조회")
    @GetMapping("/items")
    fun getFeedbackItems(
        @RequestParam(required = false) teacherId: Long?
    ): List<FeedbackItemResponse> {
        val feedbackItems = feedbackService.getFeedbackItemsByTeacherId(teacherId)

        return feedbackItems.toResponse()
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
    ): List<FeedbackItemResponse> {
        val feedbackItems = feedbackService.getFeedbackItemsByMemberId(authTokenPayload.id)

        return feedbackItems.toResponse()
    }

    @Auth
    @Operation(summary = "피드백 요청")
    @PostMapping("/{feedbackTicketId}")
    fun requestFeedback(
        @AuthPayload authTokenPayload: AuthTokenPayload,
        @PathVariable feedbackTicketId: Long,
        @RequestBody request: RequestFeedbackRequest
    ) {
        feedbackService.requestFeedback(authTokenPayload.id, request.recordId, feedbackTicketId)
    }

    @Auth
    @Operation(summary = "내 피드백 조회")
    @GetMapping("/my")
    fun listFeedbacks(
        @AuthPayload authTokenPayload: AuthTokenPayload
    ): List<FeedbackResponse> {
        val feedbacks = feedbackService.findFeedbacksByMemberId(authTokenPayload.id)

        return feedbacks.map {
            FeedbackResponse(
                id = requireNotNull(it.id),
                teacher = it.teacher,
                recordId = it.recordId.toNonNull(),
                detail = it.detail,
                createdAt = it.updatedDate
            )
        }
    }

    private fun List<FeedbackItem>.toResponse(): List<FeedbackItemResponse> {
        return this.map { feedbackItem ->
            feedbackItem.toResponse()
        }
    }

    private fun FeedbackItem.toResponse(): FeedbackItemResponse {
        return FeedbackItemResponse(
            id = this.id.toNonNull(),
            teacher = TeacherResponse(
                id = this.teacher.id.toNonNull(),
                name = this.teacher.nickname.value,
                profileImageUrl = this.teacher.profileImage,
                verified = this.teacher.verified,
                description = this.teacher.description,
                genderType = this.teacher.genderType,
                expertiseType = this.teacher.expertiseType
            ),
            description = this.description,
            price = this.price,
            amount = this.amount
        )
    }
}

data class FeedbackItemResponse(
    val id: Long,
    val teacher: TeacherResponse,
    val description: String,
    val price: Int,
    var amount: Int,
)

class TeacherResponse(
    val id: Long,
    val name: String,
    val profileImageUrl: String?,
    val verified: Boolean,
    val description: String?,
    val genderType: GenderType,
    val expertiseType: ExpertiseType,
)

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
