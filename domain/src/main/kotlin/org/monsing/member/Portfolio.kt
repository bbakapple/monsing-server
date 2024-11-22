package org.monsing.member

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Portfolio(
    @OneToOne
    @JoinColumn(name = "teacher_id")
    val teacher: Teacher,

    val url: URL
) : BaseEntity()
