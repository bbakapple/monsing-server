package org.monsing.course

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import org.monsing.member.Student
import org.monsing.member.teacher.Teacher

@Entity
class ClassRoom(
    @ManyToOne
    val teacher: Teacher,
    @ManyToOne
    val student: Student,
    var status: ClassRoomStatusType
) : BaseEntity() {
    fun complete() {
        status = ClassRoomStatusType.CLOSED
    }

    fun enter() {
        status = ClassRoomStatusType.IN_PROGRESS
    }

    companion object {
        fun create(teacher: Teacher, student: Student): ClassRoom {
            return ClassRoom(teacher, student, ClassRoomStatusType.OPEN)
        }
    }
}
