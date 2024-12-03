package org.monsing.member.teacher

interface TeacherCustomRepository {

    fun findByConditions(condition: TeacherCondition): List<Teacher>
}
