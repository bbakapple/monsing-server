package org.monsing.auth

import org.monsing.auth.jwt.TokenManager
import org.monsing.auth.jwt.TokenPayload
import org.monsing.auth.oauthhandler.OauthAdaptor
import org.monsing.member.Member
import org.monsing.member.MemberRepository
import org.monsing.member.Nickname
import org.monsing.member.OauthProviderType
import org.monsing.token.Token
import org.springframework.stereotype.Service
import java.net.URL

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val oauthAdaptor: OauthAdaptor,
    private val tokenManager: TokenManager
) {

    fun login(oauthProviderType: OauthProviderType, oauthToken: String): Token {
        val oauthIdentifier = oauthAdaptor.handle(oauthProviderType, oauthToken)
        val member = memberRepository.findByIdentifierAndOauthProviderType(oauthIdentifier.id, oauthProviderType)
            ?: throw IllegalArgumentException("Member not found")

        val id = requireNotNull(member.id) {
            "Member id must not be null"
        }

        return Token(
            accessToken = tokenManager.createAccessToken(TokenPayload(id)),
            refreshToken = tokenManager.createRefreshToken(id)
        )
    }

    fun register(registerDto: RegisterDto): Token {
        val oauthIdentifier = oauthAdaptor.handle(
            registerDto.oauthProviderType,
            registerDto.oauthToken
        )

        val member = memberRepository.save(
            Member(
                identifier = oauthIdentifier.id,
                oauthProviderType = registerDto.oauthProviderType,
                memberType = registerDto.memberType,
                nickname = Nickname(registerDto.nickname),
                genderType = registerDto.genderType,
                profileImage = URL(registerDto.profileImage)
            )
        )

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
