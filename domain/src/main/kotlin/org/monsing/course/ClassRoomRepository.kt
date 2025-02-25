package org.monsing.course

import org.springframework.data.jpa.repository.JpaRepository

interface ClassRoomRepository : JpaRepository<ClassRoom, Long> {

    fun findByStudentIdOrTeacherId(studentId: Long, teacherId: Long): List<ClassRoom>
}
