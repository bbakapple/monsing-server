package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.monsing.BaseEntity

@Entity
class Career(
    @OneToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    val period: String,

    val detail: String
) : BaseEntity()
