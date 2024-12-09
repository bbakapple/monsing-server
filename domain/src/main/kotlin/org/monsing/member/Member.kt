package org.monsing.member

import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class Member(

    val identifier: String,

    val oauthProviderType: OauthProviderType,
) : BaseEntity()
