package org.monsing.member.teacher

import jakarta.persistence.Entity
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Portfolio(

    val url: URL
) : BaseEntity()
