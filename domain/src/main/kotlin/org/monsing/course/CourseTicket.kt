package org.monsing.course

import jakarta.persistence.*
import org.monsing.BaseEntity
import org.monsing.member.Teacher

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
    @Column(name = "total_number")
    val totalNumber: ClassNumber,

    @Embedded
    val classTime: ClassTime
) : BaseEntity()
