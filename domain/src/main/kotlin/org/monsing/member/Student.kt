package org.monsing.member

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@DiscriminatorValue("student")
@Entity
class Student(

    id: Long? = null,

    identifier: String,

    oauthProviderType: OauthProviderType,

    nickname: Nickname,

    @Column(name = "profile_image")
    val profileImage: String? = null

) : Member(id, identifier, oauthProviderType, nickname)
