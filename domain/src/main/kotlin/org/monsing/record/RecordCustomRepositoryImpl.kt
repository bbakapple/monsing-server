package org.monsing.record

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.monsing.member.Student
import org.springframework.data.domain.PageRequest

class RecordCustomRepositoryImpl(
    private val jpqlExecutor: KotlinJdslJpqlExecutor
) : RecordCustomRepository {

    override fun findRecordsByMemberIdWithPaging(id: Long, size: Int?, lastId: Long?): List<Record> {
        return jpqlExecutor.findAll(
            defaultPageStrategy(size)
        ) {
            select(entity(Record::class))
                .from(
                    entity(Record::class),
                    join(entity(Student::class))
                        .on(path(Student::id).eq(path(Record::studentId)))
                )
                .whereAnd(
                    path(Record::studentId).eq(path(Student::id)),
                    path(Student::id).eq(id),
                    path(Record::id).gt(lastId ?: 0)
                )
                .orderBy(path(Record::id).asc())
        }.filterNotNull()
    }

    private fun defaultPageStrategy(size: Int?) =
        PageRequest.of(0, size ?: 10)
}
