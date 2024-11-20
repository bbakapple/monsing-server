package org.monsing.member

import jakarta.persistence.*
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Teacher(
    @OneToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    val summary: String,

    @Enumerated(EnumType.STRING)
    val strongSide: StrongSide,

    val description: String,

    val forStudent: String
) : BaseEntity() {
}

@Entity
class Career(
    @OneToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    val period: String,

    val detail: String
) : BaseEntity() {
}

@Entity
class Portfolio(
    @OneToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    val url: URL
) : BaseEntity() {
}

enum class StrongSide {
    PITCH,
    FIX,
    RHYTHM
}

@Entity
class Review(
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    @ManyToOne
    @JoinColumn(name = "writer_id")
    val writer: Student,

    val detail: String
) : BaseEntity() {
}

@Entity
class Rating(
    @ManyToOne
    @JoinColumn(name = "review_id")
    val review: Review,

    @Enumerated(EnumType.STRING)
    val category: RatingCategory,

    @Embedded
    val score: RatingScore
) : BaseEntity() {
}

enum class RatingCategory {
    KINDNESS,
    KNOWLEDGE,
    PASSION
}

@Embeddable
class RatingScore(
    @Column(name = "score")
    val value: Int
) {

}
