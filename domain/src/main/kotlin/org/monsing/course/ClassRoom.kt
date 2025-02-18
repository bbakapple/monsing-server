package org.monsing.course

import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import org.monsing.BaseEntity
import org.monsing.member.Student
import org.monsing.member.teacher.Teacher

@Entity
class ClassRoom(
    @OneToOne
    val teacher: Teacher,
    @OneToOne
    val student: Student,
    val status: ClassRoomStatusType
) : BaseEntity() {
    companion object {
        fun create(teacher: Teacher, student: Student): ClassRoom {
            return ClassRoom(teacher, student, ClassRoomStatusType.OPEN)
        }
    }
}
