package org.monsing.course

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import org.monsing.member.teacher.Teacher

@Entity
class CourseTicket(
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    val title: String,

    val detail: String,

    @Embedded
    val price: Price,

    @Embedded
    val totalNumber: ClassNumber,

    @Embedded
    val classTime: ClassTime
) : BaseEntity()
