package org.monsing.member

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Member(
    @Enumerated(EnumType.STRING)
    val memberType: MemberType,

    @Embedded
    val nickname: Nickname,

    @Enumerated(EnumType.STRING)
    val genderType: GenderType,

    @Column(name = "profile_image")
    val profileImage: URL
) : BaseEntity()
