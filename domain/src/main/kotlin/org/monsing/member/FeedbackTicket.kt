package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity

@Entity
class FeedbackTicket(
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    @ManyToOne
    @JoinColumn(name = "student_id")
    val student: Student,

    val amount: Int,
) : BaseEntity() {
}
