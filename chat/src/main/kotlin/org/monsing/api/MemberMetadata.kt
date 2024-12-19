package org.monsing.api

const val MEMBER_METADATA = "member-metadata"

data class MemberMetadata(
    val memberId: Long,
    val deviceId: String
)
