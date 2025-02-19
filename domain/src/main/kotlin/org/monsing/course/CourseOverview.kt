package org.monsing.course

import jakarta.persistence.Column


class CourseOverview(

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val curriculum: String,
)
