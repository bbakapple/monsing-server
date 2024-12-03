package org.monsing.member.teacher

import org.monsing.member.GenderType

data class TeacherCondition(
    val keyword: String?,
    val price: Int?,
    val genderType: GenderType?,
    val verified: Boolean?,
    val size: Int?,
    val lastId: Long?
)
