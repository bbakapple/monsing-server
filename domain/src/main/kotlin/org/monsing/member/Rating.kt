package org.monsing.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.monsing.BaseEntity

@Entity
class Rating(
        @ManyToOne
        @JoinColumn(name = "review_id")
        val review: Review,

        @Enumerated(EnumType.STRING)
        val category: RatingType,

        @Embedded
        val score: RatingScore
) : BaseEntity()
