package org.monsing.member

import org.monsing.member.teacher.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findByIdentifierAndOauthProviderType(id: String, oauthProviderType: OauthProviderType): Member?

    fun findTeacherById(id: Long): Teacher? {
        return findByIdOrNull(id) as? Teacher
    }

    fun findStudentById(id: Long): Student? {
        return findByIdOrNull(id) as? Student
    }
}
