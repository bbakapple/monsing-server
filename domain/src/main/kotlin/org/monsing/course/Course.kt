package org.monsing.course

import jakarta.persistence.*
import org.monsing.BaseEntity
import org.monsing.member.Teacher
import java.time.DayOfWeek
import java.time.LocalDateTime

@Entity
class Course(
    @ManyToOne
    @JoinColumn(name = "course_ticket_id")
    val courseTicket: CourseTicket,


    ) : BaseEntity() {
}

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
) : BaseEntity() {

}

@Embeddable
class ClassNumber(
    val value: Int
) {
}

@Embeddable
class Price(
    @Column(name = "price")
    val value: Int
) {
}


@Embeddable
class ClassTime(
    @Column(name = "class_time")
    val value: Int
) {
}

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
) : BaseEntity() {
}

@Entity
class CourseSchedule(
    @ManyToOne
    @JoinColumn(name = "course_ticket_id")
    val courseTicket: CourseTicket,

    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek,

    val startTime: LocalDateTime,

    val isSold: Boolean
) : BaseEntity() {
}
