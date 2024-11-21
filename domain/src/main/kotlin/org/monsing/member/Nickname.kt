package org.monsing.member

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Nickname(
    @Column(name = "nickname")
    val value: String
) {

}
