package org.monsing.auth

data class RegisterRequest(
    val nickname: String,
    val oauthToken: String,
    val oauthProviderType: String,
    val genderType: String,
    val profileImage: String,
    val memberType: String
)
