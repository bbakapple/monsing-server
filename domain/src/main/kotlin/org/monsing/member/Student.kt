package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import org.monsing.BaseEntity

@Entity
class Student(
    @OneToOne
    val member: Member
) : BaseEntity()
