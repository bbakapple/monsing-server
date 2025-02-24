package org.monsing.student

import org.monsing.member.MemberRepository
import org.monsing.member.Nickname
import org.monsing.member.Student
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun create(memberId: Long, name: String) {
        val member = memberRepository.findByIdOrElseThrow(memberId)

        memberRepository.save(
            Student(
                identifier = member.identifier,
                oauthProviderType = member.oauthProviderType,
                nickname = Nickname(name)
            )
        )
    }
}
