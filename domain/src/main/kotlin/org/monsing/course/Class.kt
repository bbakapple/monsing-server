package org.monsing.course

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import java.time.LocalDateTime

@Entity
class Class(
    @ManyToOne
    @JoinColumn(name = "course_id")
    val course: Course,

    val date: LocalDateTime,

    val teacherMemo: String,

    val studentMemo: String,

    @Embedded
    val classNumber: ClassNumber
) : BaseEntity()
