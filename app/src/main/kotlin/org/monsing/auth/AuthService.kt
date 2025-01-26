package org.monsing.auth

import org.monsing.auth.jwt.Role
import org.monsing.auth.jwt.TokenManager
import org.monsing.auth.jwt.TokenPayload
import org.monsing.auth.oauthhandler.OauthAdaptor
import org.monsing.member.Member
import org.monsing.member.MemberRepository
import org.monsing.member.OauthProviderType
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.TeacherRepository
import org.monsing.token.Token
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val oauthAdaptor: OauthAdaptor,
    private val tokenManager: TokenManager
) {

    @Transactional
    fun login(oauthProviderType: OauthProviderType, oauthToken: String): Token {
        val oauthIdentifier = oauthAdaptor.handle(oauthProviderType, oauthToken)
        val member = memberRepository.findByIdentifierAndOauthProviderType(oauthIdentifier.id, oauthProviderType)
            ?: memberRepository.save(Member(oauthIdentifier.id, oauthProviderType))

        val id = requireNotNull(member.id) {
            "Member id must not be null"
        }

        val role = findRoleByMemberId(id)

        return Token(
            accessToken = tokenManager.createAccessToken(TokenPayload(id, role)),
            refreshToken = tokenManager.createRefreshToken(id)
        )
    }

    @Transactional(readOnly = true)
    fun refresh(refreshToken: String): Token {
        val payload = tokenManager.getRefreshPayload(refreshToken)
        val role = findRoleByMemberId(payload)

        return Token(
            accessToken = tokenManager.createAccessToken(TokenPayload(payload, role)),
            refreshToken = refreshToken
        )
    }

    private fun findRoleByMemberId(id: Long): Role {
        return when {
            studentRepository.existsByMemberId(id) -> Role.STUDENT
            teacherRepository.existsByMemberId(id) -> Role.TEACHER
            else -> Role.NONE
        }
    }
}
