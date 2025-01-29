package org.monsing.record.feedback

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Lob
import org.monsing.BaseEntity

private const val MAXIMUM_LENGTH = 3000

@Entity
class Feedback(
    @Column(nullable = false)
    val teacherId: Long,

    @Lob
    private var _detail: String? = null,

    @Enumerated(EnumType.STRING)
    var status: FeedbackStatus = FeedbackStatus.REQUESTED
) : BaseEntity() {

    val detail: String
        get() = _detail ?: ""

    fun writeFeedback(detail: String) {
        require(detail.isNotBlank()) { "Detail must not be blank" }
        require(detail.length <= MAXIMUM_LENGTH) { "Detail must not exceed $MAXIMUM_LENGTH characters" }
        status = status.complete()
        this._detail = detail
    }
}
