package org.monsing.auth

import openapi.api.AuthApi
import openapi.model.OAuthLoginRequest
import openapi.model.RefreshTokenRequest
import openapi.model.TokenResponse
import org.monsing.auth.jwt.TokenPayload
import org.monsing.member.OauthProviderType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val authService: AuthService) : AuthApi {
    override fun exit(tokenPayload: TokenPayload): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun login(oauthLoginRequest: OAuthLoginRequest): ResponseEntity<TokenResponse> {
        val token = authService.login(oauthLoginRequest.providerType(), oauthLoginRequest.oauthToken)

        return ResponseEntity.ok(TokenResponse(token.accessToken, token.refreshToken))
    }

    override fun refresh(
        tokenPayload: TokenPayload,
        refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<TokenResponse> {
        val token = authService.refresh(refreshTokenRequest.refreshToken)

        return ResponseEntity.ok(TokenResponse(token.accessToken, token.refreshToken))
    }

    private fun OAuthLoginRequest.providerType() = OauthProviderType.valueOf(oauthProvider.uppercase())
}

