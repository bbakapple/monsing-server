package org.monsing.record.feedback

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.monsing.BaseEntity

@Entity
class Feedback(
    val teacherId: Long,

    var detail: String? = null,

    @Enumerated(EnumType.STRING)
    var status: FeedbackStatus = FeedbackStatus.REQUESTED
) : BaseEntity() {

    fun writeFeedback(detail: String) {
        require(detail.isNotBlank()) { "Detail must not be blank" }
        status = status.complete()
        this.detail = detail
    }
}
