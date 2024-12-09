package org.monsing.member.teacher

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.monsing.course.CourseTicket
import org.monsing.course.Price
import org.monsing.eq
import org.monsing.like
import org.monsing.lt
import org.monsing.member.Nickname
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

class TeacherCustomRepositoryImpl(
    @Autowired private val jpqlExecutor: KotlinJdslJpqlExecutor
) : TeacherCustomRepository {

    override fun findByConditions(
        genderType: GenderType?,
        verified: Boolean?,
        size: Int?,
        lastId: Long?,
        keyword: String?,
        price: Int?
    ): List<Teacher> {
        return jpqlExecutor.findAll(
            defaultPageStrategy(size)
        ) {
            select(entity(Teacher::class))
                .from(
                    entity(Teacher::class),
                    join(entity(CourseTicket::class))
                        .on(entity(Teacher::class).eq(path(CourseTicket::teacher)))
                )
                .whereAnd(
                    eq(path(Teacher::genderType), genderType),
                    eq(path(Teacher::verified), verified),
                    like(path(Teacher::nickname)(Nickname::value), keyword),
                    lt(path(CourseTicket::price)(Price::value), price),
                    path(Teacher::id).gt(lastId ?: 0L),
                )
                .orderBy(path(Teacher::id).asc())
        }.filterNotNull()
    }

    private fun defaultPageStrategy(size: Int?) =
        PageRequest.of(0, size ?: 10)
}
