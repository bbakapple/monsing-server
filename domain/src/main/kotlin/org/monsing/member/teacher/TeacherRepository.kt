package org.monsing.member.teacher

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeacherRepository : JpaRepository<Teacher, Long>, TeacherCustomRepository
