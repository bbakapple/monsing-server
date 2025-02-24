package org.monsing.teacher

import org.monsing.member.MemberRepository
import org.monsing.member.Nickname
import org.monsing.member.OauthProviderType
import org.monsing.member.TempMemberRepository
import org.monsing.member.teacher.GenderType
import org.monsing.member.teacher.Teacher
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeacherService(
    private val memberRepository: MemberRepository,
    private val tempMemberRepository: TempMemberRepository
) {

    @Transactional
    fun createTeacher(memberId: Long, name: String, genderType: GenderType?) {
        val member = tempMemberRepository.findByIdOrElseThrow(memberId)

        memberRepository.save(
            Teacher(
                id = member.id,
                identifier = member.identifier,
                oauthProviderType = member.oauthProviderType,
                nickname = Nickname(name),
                genderType = genderType ?: GenderType.OTHER
            )
        )
    }

    @Transactional(readOnly = true)
    fun findTeachersByConditions(
        genderType: GenderType?,
        verified: Boolean?,
        size: Int?,
        lastId: Long?,
        keyword: String?,
        price: Int?
    ): List<Teacher> {
//        return teacherRepository.findByConditions(genderType, verified, size, lastId, keyword, price)
        return emptyList()
    }

    @Transactional(readOnly = true)
    fun findTeacherById(id: Long): Teacher {
        return Teacher(
            identifier = "$id",
            oauthProviderType = OauthProviderType.KAKAO,
            nickname = Nickname("teacher"),
            genderType = GenderType.OTHER
        )
    }
}
