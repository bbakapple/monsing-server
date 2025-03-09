package org.monsing.record

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecordRepository : JpaRepository<Record, Long>, RecordCustomRepository{
    fun findByStudentId(id: Long): List<Record>
}
