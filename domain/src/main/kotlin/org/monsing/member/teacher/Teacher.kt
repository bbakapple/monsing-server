package org.monsing.member.teacher

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import org.monsing.member.Member
import org.monsing.member.Nickname
import org.monsing.member.OauthProviderType
import org.monsing.member.StrongSideType

@DiscriminatorValue("teacher")
@Entity
class Teacher(

    identifier: String,

    oauthProviderType: OauthProviderType,

    nickname: Nickname,

    val summary: String? = null,

    @Enumerated(EnumType.STRING)
    val strongSideType: StrongSideType? = null,

    val description: String? = null,

    val forStudent: String? = null,

    val verified: Boolean = false,

    @Column(name = "profile_image")
    val profileImage: String? = null,

    @Enumerated(EnumType.STRING)
    val genderType: GenderType,

    @OneToMany
    val portfolios: List<Portfolio> = mutableListOf(),

    @OneToMany
    val careers: List<Career> = mutableListOf()

) : Member(identifier, oauthProviderType, nickname)
