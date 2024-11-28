package org.monsing.auth

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
