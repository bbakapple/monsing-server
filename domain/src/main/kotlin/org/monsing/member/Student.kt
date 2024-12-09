package org.monsing.member

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class Student(

    val memberId: Long,

    @Column(name = "profile_image")
    val profileImage: String? = null,

    @Embedded
    val nickname: Nickname,
) : BaseEntity()
