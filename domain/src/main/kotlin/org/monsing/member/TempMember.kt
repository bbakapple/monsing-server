package org.monsing.member

import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class TempMember(

    val identifier: String,

    val oauthProviderType: OauthProviderType,
) : BaseEntity()
