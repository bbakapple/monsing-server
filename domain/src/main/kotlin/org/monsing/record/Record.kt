package org.monsing.record

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.monsing.BaseEntity
import org.monsing.member.Member
import org.monsing.member.teacher.Teacher
import org.monsing.record.feedback.Feedback
import org.monsing.record.feedback.FeedbackStatus

@Entity
class Record(

    id: Long? = null,

    title: String,

    @Column(nullable = false)
    val studentId: Long,

    @Column(nullable = false)
    val fileKey: String,

    @OneToMany
    val feedbacks: MutableList<Feedback> = mutableListOf()
) : BaseEntity(id = id) {

    @Embedded
    private var _title = RecordTitle(title)

    val title
        get() = _title.value

    val notCompletedFeedBacks
        get() = feedbacks.filter { it.status != FeedbackStatus.COMPLETED }

    fun requestFeedback(teacher: Teacher) {
        require(feedbacks.requestedBy(teacher).not()) { "Feedback already requested" }
        feedbacks.add(Feedback(recordId = requireNotNull(id), teacher = teacher))
    }

    private fun List<Feedback>.requestedBy(teacher: Teacher): Boolean {
        return any { it.teacher == teacher }
    }

    fun updateTitle(title: String) {
        _title = RecordTitle(title)
    }

    fun isOwnedBy(member: Member?): Boolean {
        return studentId == member?.id || feedbacks.any { it.teacher.id == member?.id }
    }
}
