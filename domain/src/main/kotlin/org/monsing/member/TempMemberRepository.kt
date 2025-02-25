package org.monsing.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TempMemberRepository : JpaRepository<TempMember, Long> {
    fun findByIdentifierAndOauthProviderType(id: String, oauthProviderType: OauthProviderType): TempMember?
}
