package org.monsing.auth

import openapi.api.AuthApi
import openapi.model.OAuthLoginRequest
import openapi.model.RefreshTokenRequest
import openapi.model.TokenResponse
import org.monsing.auth.jwt.TokenPayload
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController : AuthApi {
    override fun exit(tokenPayload: TokenPayload): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun login(oauthLoginRequest: OAuthLoginRequest): ResponseEntity<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun refresh(
        tokenPayload: TokenPayload,
        refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<TokenResponse> {
        TODO("Not yet implemented")
    }
}
