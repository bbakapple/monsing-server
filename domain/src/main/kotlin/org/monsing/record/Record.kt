package org.monsing.record

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.monsing.BaseEntity
import org.monsing.record.feedback.Feedback

@Entity
class Record(
    val studentId: Long,

    val fileKey: String,

    @OneToMany(cascade = [CascadeType.ALL])
    val feedbacks: MutableList<Feedback> = mutableListOf()
) : BaseEntity() {

    fun requestFeedback(teacherId: Long) {
        require(feedbacks.none { it.teacherId == teacherId }) { "Feedback already requested" }
        feedbacks.add(Feedback(teacherId = teacherId))
    }

    fun containsTeacherFeedback(teacherId: Long): Boolean {
        return feedbacks.any { it.teacherId == teacherId }
    }
}
