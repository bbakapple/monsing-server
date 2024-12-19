package org.monsing.member.teacher

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import org.monsing.BaseEntity
import org.monsing.member.Nickname
import org.monsing.member.StrongSideType

@Entity
class Teacher(

    val memberId: Long,

    val summary: String,

    @Enumerated(EnumType.STRING)
    val strongSideType: StrongSideType,

    val description: String,

    val forStudent: String,

    val verified: Boolean,

    @Embedded
    val nickname: Nickname,

    @Column(name = "profile_image")
    val profileImage: String? = null,

    @Enumerated(EnumType.STRING)
    val genderType: GenderType,

    @OneToMany
    val portfolios: List<Portfolio> = mutableListOf(),

    @OneToMany
    val careers: List<Career> = mutableListOf()
) : BaseEntity()
