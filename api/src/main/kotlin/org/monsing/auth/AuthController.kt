package org.monsing.auth

import openapi.api.AuthApi
import openapi.model.OAuthLoginRequest
import openapi.model.RefreshTokenRequest
import openapi.model.TokenResponse
import org.monsing.auth.jwt.TokenPayload
import org.monsing.member.OauthProviderType
import org.monsing.util.enumValueOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val authService: AuthService) : AuthApi {
    override fun exit(tokenPayload: TokenPayload): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }

    override fun login(oauthLoginRequest: OAuthLoginRequest): ResponseEntity<TokenResponse> {
        val oauthProviderType = requireNotNull(oauthLoginRequest.oauthProviderType()) {
            "Invalid oauth provider type ${oauthLoginRequest.oauthProvider.name}"
        }
        val token = authService.login(oauthProviderType, oauthLoginRequest.oauthToken)

        return ResponseEntity.ok(TokenResponse(token.accessToken, token.refreshToken))
    }

    override fun refresh(
        tokenPayload: TokenPayload,
        refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<TokenResponse> {
        val token = authService.refresh(refreshTokenRequest.refreshToken)

        return ResponseEntity.ok(TokenResponse(token.accessToken, token.refreshToken))
    }

    private fun OAuthLoginRequest.oauthProviderType(): OauthProviderType? {
        return enumValueOrNull<OauthProviderType>(oauthProvider.name.uppercase())
    }
}

