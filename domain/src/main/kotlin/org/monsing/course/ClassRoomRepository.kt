package org.monsing.course

import org.springframework.data.jpa.repository.JpaRepository

interface ClassRoomRepository : JpaRepository<ClassRoom, Long> {

    fun findByLessonId(lessonId: Long): List<ClassRoom>
}
