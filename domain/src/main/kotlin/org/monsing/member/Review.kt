package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity

@Entity
class Review(
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    @ManyToOne
    @JoinColumn(name = "writer_id")
    val writer: Student,

    val detail: String
) : BaseEntity()
