package org.monsing.course

enum class LessonStatusType {
    AVAILABLE,
    NOT_AVAILABLE,
    RESERVED,
    ;

    fun isAvailable() = this == AVAILABLE
}
