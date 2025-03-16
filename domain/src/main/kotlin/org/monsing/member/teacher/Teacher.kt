package org.monsing.member.teacher

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.hibernate.annotations.BatchSize
import org.monsing.member.Member
import org.monsing.member.Nickname
import org.monsing.member.OauthProviderType
import org.monsing.member.StrongSideType

@DiscriminatorValue("teacher")
@Entity
class Teacher(

    id: Long? = null,

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
    var genderType: GenderType = GenderType.OTHER,

    @Enumerated(EnumType.STRING)
    var expertiseType: ExpertiseType = ExpertiseType.NONE,

    @BatchSize(size = 5)
    @OneToMany(fetch = FetchType.EAGER)
    val portfolios: List<Portfolio> = mutableListOf(),

    @BatchSize(size = 5)
    @OneToMany(fetch = FetchType.EAGER)
    val careers: List<Career> = mutableListOf()

) : Member(id, identifier, oauthProviderType, nickname) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Teacher) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
