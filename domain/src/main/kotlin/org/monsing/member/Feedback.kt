package org.monsing.member

import jakarta.persistence.*
import org.monsing.BaseEntity

@Entity
class Feedback(
    @ManyToOne
    val teacher: Teacher,

    @OneToOne
    val record: Record,

    val detail: String,

    @Enumerated(EnumType.STRING)
    val status: FeedbackStatus
) : BaseEntity() {
}
