package org.monsing.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByIdentifierAndOauthProviderType(id: String, oauthProviderType: OauthProviderType): Member?
}
