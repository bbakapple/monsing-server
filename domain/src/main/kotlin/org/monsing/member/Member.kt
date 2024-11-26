package org.monsing.member

import jakarta.persistence.*
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Member(

    val identifier: String,

    val oauthProviderType: OauthProviderType,

    @Enumerated(EnumType.STRING)
    val memberType: MemberType,

    @Embedded
    val nickname: Nickname,

    @Enumerated(EnumType.STRING)
    val genderType: GenderType,

    @Column(name = "profile_image")
    val profileImage: URL
) : BaseEntity()
