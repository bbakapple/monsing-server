package org.monsing.auth

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
        val oauthInfo = oauthAdaptor.handle(oauthProviderType, oauthToken)
        val member = memberRepository.findByIdentifierAndOauthProviderType(oauthInfo.id, oauthProviderType)
            ?: memberRepository.save(
                Member(
                    identifier = oauthInfo.id,
                    oauthProviderType = oauthProviderType,
                    memberType = MemberType.STUDENT,
                    nickname = Nickname("dd"),
                    genderType = GenderType.OTHER,
                    profileImage = URL("asd")
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
