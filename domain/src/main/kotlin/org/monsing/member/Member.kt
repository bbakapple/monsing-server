package org.monsing.member

import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "member_type")
@Entity
class Member(

    @Id
    var id: Long? = null,

    val identifier: String,

    val oauthProviderType: OauthProviderType,

    @Embedded
    var nickname: Nickname = Nickname(),

    @CreatedDate
    val createdDate: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    val updatedDate: LocalDateTime = LocalDateTime.now()
)
