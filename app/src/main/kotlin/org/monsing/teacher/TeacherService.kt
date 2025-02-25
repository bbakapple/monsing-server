package org.monsing.teacher

import org.monsing.member.MemberRepository
import org.monsing.member.Nickname
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
}
