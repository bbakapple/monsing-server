package org.monsing.record.feedback

import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class FeedbackTicket(

    val teacherId: Long,

    val studentId: Long,

    var amount: Int,
) : BaseEntity() {

    fun decreaseAmount() {
        require(amount > 0) { "Amount must be greater than 0" }
        amount--
    }
}
