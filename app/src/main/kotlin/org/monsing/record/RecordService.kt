package org.monsing.record

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecordService(
    private val recordRepository: RecordRepository
) {

    @Transactional
    fun saveRecord(record: Record): Record {
        return recordRepository.save(record)
    }
}
