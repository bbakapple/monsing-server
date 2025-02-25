package org.monsing.token

data class AuthToken(
    val accessToken: String,
    val refreshToken: String
)
