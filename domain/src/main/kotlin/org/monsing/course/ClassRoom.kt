package org.monsing.course

import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class ClassRoom(
    val lessonId: Long,
    var status: ClassRoomStatusType = ClassRoomStatusType.OPEN
) : BaseEntity() {
    fun complete() {
        status = ClassRoomStatusType.CLOSED
    }

    fun enter() {
        status = ClassRoomStatusType.IN_PROGRESS
    }
}
