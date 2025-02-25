package org.monsing.auth

import org.monsing.auth.jwt.AuthTokenManager
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.auth.oauthhandler.OauthAdaptor
import org.monsing.member.OauthProviderType
import org.monsing.member.TempMember
import org.monsing.member.TempMemberRepository
import org.monsing.token.AuthToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val tempMemberRepository: TempMemberRepository,
    private val oauthAdaptor: OauthAdaptor,
    private val authTokenManager: AuthTokenManager
) {

    @Transactional
    fun login(oauthProviderType: OauthProviderType, oauthToken: String): AuthToken {
        val oauthIdentifier = oauthAdaptor.handle(oauthProviderType, oauthToken)
        val member = tempMemberRepository.findByIdentifierAndOauthProviderType(oauthIdentifier.id, oauthProviderType)
            ?: tempMemberRepository.save(
                TempMember(
                    identifier = oauthIdentifier.id,
                    oauthProviderType = oauthProviderType
                )
            )

        val id = requireNotNull(member.id) {
            "Member id must not be null"
        }

        return AuthToken(
            accessToken = authTokenManager.createAccessToken(AuthTokenPayload(id)),
            refreshToken = authTokenManager.createRefreshToken(id)
        )
    }

    @Transactional(readOnly = true)
    fun refresh(refreshToken: String): AuthToken {
        val payload = authTokenManager.getRefreshPayload(refreshToken)

        return AuthToken(
            accessToken = authTokenManager.createAccessToken(AuthTokenPayload(payload)),
            refreshToken = refreshToken
        )
    }
}
