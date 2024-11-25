package org.monsing.member

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class RatingScore(
    @Column(name = "score")
    val value: Int
)
