package org.monsing.record

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.monsing.BaseEntity
import org.monsing.record.feedback.Feedback
import org.monsing.record.feedback.FeedbackStatus

@Entity
class Record(
    title: String,

    @Column(nullable = false)
    val studentId: Long,

    @Column(nullable = false)
    val fileKey: String,

    @OneToMany
    val feedbacks: MutableList<Feedback> = mutableListOf()
) : BaseEntity() {

    @Embedded
    private var _title = RecordTitle(title)

    val title
        get() = _title.value

    val notCompletedFeedBacks
        get() = feedbacks.filter { it.status != FeedbackStatus.COMPLETED }

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

    fun updateTitle(title: String) {
        _title = RecordTitle(title)
    }
}
