package org.monsing.record

interface RecordCustomRepository {

    fun findRecordsByMemberIdWithPaging(id: Long, size: Int?, lastId: Long?): List<Record>
}
