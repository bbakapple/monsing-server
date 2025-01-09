package org.monsing.record.feedback

import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class FeedbackTicket(

    val teacherId: Long,

    val studentId: Long,

    private var _amount: Int,
) : BaseEntity() {

    val amount: Int
        get() = _amount

    fun decreaseAmount() {
        require(_amount > 0) { "Amount must be greater than 0" }
        _amount--
    }

    fun increaseAmount() {
        _amount++
    }
}
