package org.monsing.student

import org.monsing.member.MemberRepository
import org.monsing.member.Nickname
import org.monsing.member.Student
import org.monsing.member.TempMemberRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(
    private val memberRepository: MemberRepository,
    private val tempMemberRepository: TempMemberRepository
) {

    @Transactional
    fun create(memberId: Long, name: String) {
        val member = tempMemberRepository.findByIdOrElseThrow(memberId)

        memberRepository.save(
            Student(
                id = member.id,
                identifier = member.identifier,
                oauthProviderType = member.oauthProviderType,
                nickname = Nickname(name)
            )
        )
    }
}
