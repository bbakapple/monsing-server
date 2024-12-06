package org.monsing.member.teacher

import jakarta.persistence.Entity
import org.monsing.BaseEntity

@Entity
class Portfolio(

    val url: String
) : BaseEntity()
