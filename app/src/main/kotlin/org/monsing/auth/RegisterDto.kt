package org.monsing.auth

import org.monsing.member.GenderType
import org.monsing.member.MemberType
import org.monsing.member.OauthProviderType

data class RegisterDto(
    val nickname: String,
    val oauthToken: String,
    val oauthProviderType: OauthProviderType,
    val genderType: GenderType,
    val profileImage: String,
    val memberType: MemberType
)
