package org.monsing.record

interface RecordCustomRepository {

    fun findByMemberIdWithPaging(id: Long, size: Int?, lastId: Long?): List<Record>
}
