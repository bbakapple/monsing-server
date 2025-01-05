package org.monsing.record

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import java.net.URL
import org.monsing.member.Student

@Entity
class Record(
    val studentId: Long,

    val url: String
) : BaseEntity()
