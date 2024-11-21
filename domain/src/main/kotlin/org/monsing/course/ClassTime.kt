package org.monsing.course

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.Duration

@Embeddable
class ClassTime(
    @Column(name = "class_time")
    val value: Duration
) {
}
