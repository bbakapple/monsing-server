package org.monsing.auth.jwt

import com.fasterxml.jackson.annotation.JsonCreator

data class AuthTokenPayload @JsonCreator constructor(
    val id: Long
)
