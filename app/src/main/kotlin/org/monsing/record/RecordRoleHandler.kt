package org.monsing.record

import org.monsing.auth.jwt.Role

interface RecordRoleHandler {

    fun canHandle(role: Role): Boolean

    fun findRecordsByMemberId(memberId: Long, size: Int?, lastId: Long?): List<Record>

    fun validateRecordOwnership(memberId: Long, record: Record)
}
