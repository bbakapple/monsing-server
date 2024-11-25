package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
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
) : BaseEntity()
