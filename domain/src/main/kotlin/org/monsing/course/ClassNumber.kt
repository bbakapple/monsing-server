package org.monsing.course

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class ClassNumber(
    @Column(name = "total_number")
    val value: Int
)
