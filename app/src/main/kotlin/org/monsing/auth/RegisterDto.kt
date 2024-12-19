package org.monsing.auth

import org.monsing.member.OauthProviderType
import org.monsing.member.teacher.GenderType

data class RegisterDto(
    val nickname: String,
    val oauthToken: String,
    val oauthProviderType: OauthProviderType,
    val genderType: GenderType,
    val profileImage: String,
)
