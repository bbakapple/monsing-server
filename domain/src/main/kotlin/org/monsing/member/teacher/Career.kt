package org.monsing.member.teacher

import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class Career(

    val period: String,

    val detail: String
) : BaseEntity()
