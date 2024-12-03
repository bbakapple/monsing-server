package org.monsing.member

import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : org.springframework.data.repository.Repository<Member, Long> {

    fun findByIdentifierAndOauthProviderType(id: String, oauthProviderType: OauthProviderType): Member?

    fun findById(id: Long): Member?

    fun save(member: Member): Member
}
