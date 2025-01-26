package org.monsing.auth.jwt

data class TokenPayload(
    val id: Long,
    val role: Role = Role.NONE
)
