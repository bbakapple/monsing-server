package org.monsing.auth

import openapi.api.AuthApi
import openapi.model.ExpertiseType
import openapi.model.GenderType
import openapi.model.MemberRole
import openapi.model.MyInfoResponse
import openapi.model.OAuthLoginRequest
import openapi.model.RefreshTokenRequest
import openapi.model.StudentInfo
import openapi.model.TeacherInfo
import openapi.model.TokenResponse
import org.monsing.auth.jwt.AuthTokenPayload
import org.monsing.member.OauthProviderType
import org.monsing.member.Student
import org.monsing.member.teacher.Teacher
import org.monsing.util.enumValueOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val authService: AuthService) : AuthApi {
    override fun exit(authTokenPayload: AuthTokenPayload): ResponseEntity<Unit> {
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
        authTokenPayload: AuthTokenPayload,
        refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<TokenResponse> {
        val token = authService.refresh(refreshTokenRequest.refreshToken)

        return ResponseEntity.ok(TokenResponse(token.accessToken, token.refreshToken))
    }

    private fun OAuthLoginRequest.oauthProviderType(): OauthProviderType? {
        return enumValueOrNull<OauthProviderType>(oauthProvider.name.uppercase())
    }

    override fun getMyInfo(tokenPayload: AuthTokenPayload): ResponseEntity<MyInfoResponse> {
        val member = authService.getMember(tokenPayload.id)
            ?: return ResponseEntity.ok(MyInfoResponse(tokenPayload.id, MemberRole.NONE))

        if (member is Teacher) {
            return ResponseEntity.ok(
                MyInfoResponse(
                    tokenPayload.id,
                    MemberRole.TEACHER,
                    teacherInfo = TeacherInfo(
                        member.summary,
                        member.strongSideType?.name,
                        member.description,
                        member.forStudent,
                        member.verified,
                        member.profileImage,
                        GenderType.valueOf(member.genderType.name.uppercase()),
                        ExpertiseType.valueOf(member.expertiseType.name.uppercase()),
                    )
                )
            )
        }

        val student = member as Student

        return ResponseEntity.ok(
            MyInfoResponse(
                tokenPayload.id,
                MemberRole.STUDENT,
                studentInfo = StudentInfo(
                    student.nickname.value,
                )
            )
        )
    }
}

