package org.monsing.record

interface RecordCustomRepository {

    fun findStudentRecordsByMemberIdWithPaging(id: Long, size: Int?, lastId: Long?): List<Record>
    fun findTeacherRecordsByMemberIdWithPaging(id: Long, size: Int?, lastId: Long?): List<Record>
}
