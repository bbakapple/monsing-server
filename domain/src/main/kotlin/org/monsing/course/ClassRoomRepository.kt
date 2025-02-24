package org.monsing.course

import org.springframework.data.jpa.repository.JpaRepository

interface ClassRoomRepository : JpaRepository<ClassRoom, Long> {
    fun findByStudentId(id: Long): List<ClassRoom>
    fun findByTeacherId(id: Long): List<ClassRoom>

    fun findByStudentIdOrTeacherId(studentId: Long, teacherId: Long): List<ClassRoom>
}
