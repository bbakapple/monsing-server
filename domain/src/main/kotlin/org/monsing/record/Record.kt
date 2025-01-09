package org.monsing.record

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.monsing.BaseEntity
import org.monsing.record.feedback.Feedback

@Entity
class Record(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val studentId: Long,

    @Column(nullable = false)
    val fileKey: String,

    @OneToMany
    val feedbacks: MutableList<Feedback> = mutableListOf()
) : BaseEntity() {

    init {
        require(title.length <= 30) {
            "Title must not be longer than 30 characters"
        }
    }

    fun requestFeedback(teacherId: Long) {
        require(feedbacks.requestedBy(teacherId).not()) { "Feedback already requested" }
        feedbacks.add(Feedback(teacherId = teacherId))
    }

    fun containsTeacherFeedback(teacherId: Long): Boolean {
        return feedbacks.requestedBy(teacherId)
    }

    private fun List<Feedback>.requestedBy(teacherId: Long): Boolean {
        return any { it.teacherId == teacherId }
    }
}
