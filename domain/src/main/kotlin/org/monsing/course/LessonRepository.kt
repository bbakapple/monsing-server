package org.monsing.course

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LessonRepository : JpaRepository<Lesson, Long> {

    @Query(
        """
        SELECT l
        FROM Course c
        JOIN c.lessons l
        WHERE c.teacherId = :teacherId
        AND l.id = :id
        """
    )
    fun findByTeacherIdAndLessonId(teacherId: Long, id: Long): Lesson?

    @Query(
        """
        SELECT COUNT(l) > 0
        FROM Course c
        JOIN c.lessons l
        WHERE c.teacherId = :teacherId
        AND l.id = :id
        """
    )
    fun existsByTeacherIdAndLessonId(teacherId: Long, id: Long): Boolean
}
