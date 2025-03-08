package org.monsing.record.feedback

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import org.monsing.member.Student
import org.monsing.member.teacher.Teacher

@Entity
class FeedbackTicket(

    @ManyToOne
    @JoinColumn(nullable = false)
    val feedbackItem: FeedbackItem,

    @ManyToOne
    @JoinColumn(nullable = false)
    var student: Student?,

    private var _amount: Int,

) : BaseEntity() {

    val amount: Int
        get() = _amount

    fun decreaseAmount(purchaseAmount: Int) {
        require(_amount >= purchaseAmount) { "Amount must be greater than or equal to purchase amount" }
        require(_amount > 0) { "Amount must be greater than 0" }
        _amount -= purchaseAmount
    }

    fun increaseAmount() {
        _amount++
    }
}
