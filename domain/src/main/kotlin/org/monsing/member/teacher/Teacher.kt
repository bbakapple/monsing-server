package org.monsing.member.teacher

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.monsing.BaseEntity
import org.monsing.member.Member
import org.monsing.member.StrongSideType

@Entity
class Teacher(
    @OneToOne
    @JoinColumn(name = "member_id")
    val member: Member,

    val summary: String,

    @Enumerated(EnumType.STRING)
    val strongSideType: StrongSideType,

    val description: String,

    val forStudent: String,

    val verified: Boolean,

    @OneToMany
    val portfolios: List<Portfolio> = mutableListOf(),

    @OneToMany
    val careers: List<Career> = mutableListOf()

) : BaseEntity()
