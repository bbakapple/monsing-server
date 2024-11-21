package org.monsing.member

import jakarta.persistence.*
import org.monsing.BaseEntity

@Entity
class Teacher(
    @OneToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    val summary: String,

    @Enumerated(EnumType.STRING)
    val strongSideType: StrongSideType,

    val description: String,

    val forStudent: String
) : BaseEntity() {
}
