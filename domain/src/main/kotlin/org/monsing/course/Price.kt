package org.monsing.course

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Price(
    @Column(name = "price")
    val value: Int
)
