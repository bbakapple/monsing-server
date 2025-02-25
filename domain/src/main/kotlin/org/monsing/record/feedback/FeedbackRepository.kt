package org.monsing.record.feedback

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findByTeacherId(id: Long): List<Feedback>
}
