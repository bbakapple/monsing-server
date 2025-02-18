package org.monsing.auth.jwt

data class AuthTokenPayload(
    val id: Long,
    val role: Role = Role.NONE
)
