package org.monsing.record.feedback

enum class FeedbackStatus {
    REQUESTED,
    COMPLETED,
    ;

    fun complete(): FeedbackStatus {
        require(this != COMPLETED) { "Feedback is already completed" }
        return COMPLETED
    }
}
