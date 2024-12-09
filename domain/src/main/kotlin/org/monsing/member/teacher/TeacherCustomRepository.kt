package org.monsing.member.teacher

interface TeacherCustomRepository {

    fun findByConditions(
        genderType: GenderType?,
        verified: Boolean?,
        size: Int?,
        lastId: Long?,
        keyword: String?,
        price: Int?
    ): List<Teacher>
}
