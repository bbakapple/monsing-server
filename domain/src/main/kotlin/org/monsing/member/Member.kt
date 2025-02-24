package org.monsing.member

import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import org.monsing.BaseEntity

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "member_type")
@Entity
class Member(

    val identifier: String,

    val oauthProviderType: OauthProviderType,

    @Embedded
    var nickname: Nickname,
) : BaseEntity()
