package org.monsing.record.feedback

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import org.monsing.member.teacher.Teacher

private const val MAXIMUM_LENGTH = 3000

@Entity
class Feedback(
    @ManyToOne
    @JoinColumn(nullable = false)
    val teacher: Teacher,

    @Column(nullable = false)
    val recordId: Long,

    @Lob
    private var _detail: String? = null,

    @Enumerated(EnumType.STRING)
    var status: FeedbackStatus = FeedbackStatus.REQUESTED,

    var amount : Int
) : BaseEntity() {

    val detail: String
        get() = _detail ?: ""

    fun writeFeedback(detail: String) {
        require(amount > 0) { "Amount must be greater than 0" }
        require(detail.isNotBlank()) { "Detail must not be blank" }
        require(detail.length <= MAXIMUM_LENGTH) { "Detail must not exceed $MAXIMUM_LENGTH characters" }
        status = status.complete()
        this._detail = detail
        amount--
    }
}
