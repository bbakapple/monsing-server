package org.monsing.member

import jakarta.persistence.*
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Student(
    @OneToOne
    val member: Member
) : BaseEntity() {
}








