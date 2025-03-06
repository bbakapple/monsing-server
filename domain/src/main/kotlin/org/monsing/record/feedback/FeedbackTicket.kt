package org.monsing.record.feedback

import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class FeedbackTicket(

    val teacherId: Long,

    @Column(nullable = true)
    var studentId: Long?,

    private var _amount: Int,

    val price: Int,
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
