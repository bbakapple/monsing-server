package org.monsing.course

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import org.monsing.BaseEntity
import org.monsing.member.Student

@Entity
class Course(
    @OneToOne
    @JoinColumn(name = "course_schedule_id")
    val courseSchedule: CourseSchedule,

    @ManyToOne
    @JoinColumn(name = "student_id")
    val student: Student
) : BaseEntity() {
}
