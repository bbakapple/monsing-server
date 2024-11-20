package org.monsing.member

import jakarta.persistence.*
import org.monsing.BaseEntity
import java.net.URL

@Entity
class Member(
    @Enumerated(EnumType.STRING)
    val memberType: MemberType,

    @Embedded
    val nickname: Nickname,

    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(name = "profile_image")
    val profileImage: URL
) : BaseEntity() {

}

@Embeddable
class Nickname(
    @Column(name = "nickname")
    val value: String
) {

}

enum class MemberType {
    STUDENT,
    TEACHER
}

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}