package org.monsing.member

import jakarta.persistence.*
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Student(
    @OneToOne
    val member: Member
) : BaseEntity() {
}

@Entity
class Record(
    @ManyToOne
    val student: Student,

    val url: URL
) : BaseEntity() {
}

@Entity
class Feedback(
    @ManyToOne
    val student: Student,

    @OneToOne
    val record: Record,

    val detail: String,

    @Enumerated(EnumType.STRING)
    val status: FeedbackStatus
) : BaseEntity() {
}

enum class FeedbackStatus {
    REQUESTED,
    COMPLETED
}

@Entity
class FeedbackTicket(
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    @ManyToOne
    @JoinColumn(name = "student_id")
    val student: Student,

    val amount: Int,
) : BaseEntity() {
}
