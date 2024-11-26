package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Record(
    @ManyToOne
    val student: Student,

    val url: URL
) : BaseEntity()
