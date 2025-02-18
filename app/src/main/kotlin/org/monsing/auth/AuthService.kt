package org.monsing.auth

import org.monsing.auth.jwt.Role
import org.monsing.auth.jwt.AuthTokenManager
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.auth.oauthhandler.OauthAdaptor
import org.monsing.member.Member
import org.monsing.member.MemberRepository
import org.monsing.member.OauthProviderType
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.TeacherRepository
import org.monsing.token.AuthToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val oauthAdaptor: OauthAdaptor,
    private val authTokenManager: AuthTokenManager
) {

    @Transactional
    fun login(oauthProviderType: OauthProviderType, oauthToken: String): AuthToken {
        val oauthIdentifier = oauthAdaptor.handle(oauthProviderType, oauthToken)
        val member = memberRepository.findByIdentifierAndOauthProviderType(oauthIdentifier.id, oauthProviderType)
            ?: memberRepository.save(Member(oauthIdentifier.id, oauthProviderType))

        val id = requireNotNull(member.id) {
            "Member id must not be null"
        }

        val role = findRoleByMemberId(id)

        return AuthToken(
            accessToken = authTokenManager.createAccessToken(AuthTokenPayload(id, role)),
            refreshToken = authTokenManager.createRefreshToken(id)
        )
    }

    @Transactional(readOnly = true)
    fun refresh(refreshToken: String): AuthToken {
        val payload = authTokenManager.getRefreshPayload(refreshToken)
        val role = findRoleByMemberId(payload)

        return AuthToken(
            accessToken = authTokenManager.createAccessToken(AuthTokenPayload(payload, role)),
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
