package org.monsing.record

import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.monsing.BaseEntity
import org.monsing.record.feedback.Feedback

@Entity
class Record(
    val studentId: Long,

    val url: String,

    @OneToMany
    val feedbacks: List<Feedback> = mutableListOf()
) : BaseEntity()
