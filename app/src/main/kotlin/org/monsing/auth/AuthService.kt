package org.monsing.auth

import org.monsing.auth.oauth.OauthAdaptor
import org.monsing.jwt.TokenManager
import org.monsing.jwt.TokenPayload
import org.monsing.member.GenderType
import org.monsing.member.Member
import org.monsing.member.MemberRepository
import org.monsing.member.MemberType
import org.monsing.member.Nickname
import org.monsing.member.OauthProviderType
import org.springframework.stereotype.Service
import java.net.URL

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val oauthAdaptor: OauthAdaptor,
    private val tokenManager: TokenManager
) {

    fun login(oauthProviderType: OauthProviderType, oauthToken: String): TokenResponse {
        val oauthIdentifier = oauthAdaptor.handle(oauthProviderType, oauthToken)
        val member = memberRepository.findByIdentifierAndOauthProviderType(oauthIdentifier.id, oauthProviderType)
            ?: throw IllegalArgumentException("Member not found")

        val id = requireNotNull(member.id) {
            "Member id must not be null"
        }

        return TokenResponse(
            accessToken = tokenManager.createAccessToken(TokenPayload(id)),
            refreshToken = tokenManager.createRefreshToken(id)
        )
    }

    fun register(registerRequest: RegisterRequest): TokenResponse {
        val oauthIdentifier = oauthAdaptor.handle(
            OauthProviderType.valueOf(registerRequest.oauthProviderType),
            registerRequest.oauthToken
        )

        val member = memberRepository.save(
            Member(
                identifier = oauthIdentifier.id,
                oauthProviderType = OauthProviderType.valueOf(registerRequest.oauthProviderType),
                memberType = MemberType.valueOf(registerRequest.memberType),
                nickname = Nickname(registerRequest.nickname),
                genderType = GenderType.valueOf(registerRequest.genderType),
                profileImage = URL(registerRequest.profileImage)
            )
        )

        val id = member.id ?: throw IllegalStateException("Member id must not be null")

        return TokenResponse(
            accessToken = tokenManager.createAccessToken(TokenPayload(id)),
            refreshToken = tokenManager.createRefreshToken(id)
        )
    }

    fun refresh(refreshToken: String): TokenResponse {
        val payload = tokenManager.getRefreshPayload(refreshToken)

        return TokenResponse(
            accessToken = tokenManager.createAccessToken(TokenPayload(payload)),
            refreshToken = refreshToken
        )
    }
}
