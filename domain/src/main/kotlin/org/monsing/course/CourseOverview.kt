package org.monsing.course

import jakarta.persistence.Column


class CourseOverview(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var curriculum: String,
) {

    fun update(
        name: String? = null,
        description: String? = null,
        curriculum: String? = null
    ) {
        name?.let { this.name = it }
        description?.let { this.description = it }
        curriculum?.let { this.curriculum = it }
    }
}
