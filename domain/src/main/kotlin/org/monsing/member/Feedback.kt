package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
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
) : BaseEntity()
