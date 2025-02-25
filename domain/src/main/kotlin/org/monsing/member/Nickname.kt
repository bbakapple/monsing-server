package org.monsing.member

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.util.UUID

@Embeddable
class Nickname(
    @Column(name = "nickname", nullable = false, unique = true)
    val value: String = UUID.randomUUID().toString()
)
