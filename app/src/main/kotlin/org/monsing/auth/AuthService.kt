package org.monsing.auth

import org.monsing.auth.jwt.TokenManager
import org.monsing.auth.jwt.TokenPayload
import org.monsing.auth.oauthhandler.OauthAdaptor
import org.monsing.member.Member
import org.monsing.member.MemberRepository
import org.monsing.member.OauthProviderType
import org.monsing.token.Token
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val oauthAdaptor: OauthAdaptor,
    private val tokenManager: TokenManager
) {

    fun login(oauthProviderType: OauthProviderType, oauthToken: String): Token {
        val oauthIdentifier = oauthAdaptor.handle(oauthProviderType, oauthToken)
        val member = memberRepository.findByIdentifierAndOauthProviderType(oauthIdentifier.id, oauthProviderType)
            ?: memberRepository.save(Member(oauthIdentifier.id, oauthProviderType))

        val id = requireNotNull(member.id) {
            "Member id must not be null"
        }

        return Token(
            accessToken = tokenManager.createAccessToken(TokenPayload(id)),
            refreshToken = tokenManager.createRefreshToken(id)
        )
    }

    fun refresh(refreshToken: String): Token {
        val payload = tokenManager.getRefreshPayload(refreshToken)

        return Token(
            accessToken = tokenManager.createAccessToken(TokenPayload(payload)),
            refreshToken = refreshToken
        )
    }
}
