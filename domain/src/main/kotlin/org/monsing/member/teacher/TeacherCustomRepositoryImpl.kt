package org.monsing.member.teacher

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.monsing.course.CourseTicket
import org.monsing.course.Price
import org.monsing.eq
import org.monsing.like
import org.monsing.lt
import org.monsing.member.Member
import org.monsing.member.Nickname
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

class TeacherCustomRepositoryImpl(
    @Autowired private val jpqlExecutor: KotlinJdslJpqlExecutor
) : TeacherCustomRepository {

    override fun findByConditions(condition: TeacherCondition): List<Teacher> {
        return jpqlExecutor.findAll(
            defaultPageStrategy(condition)
        ) {
            select(entity(Teacher::class))
                .from(
                    entity(Teacher::class),
                    join(entity(Member::class))
                        .on(path(Teacher::member).eq(entity(Member::class))),
                    join(entity(CourseTicket::class))
                        .on(entity(Teacher::class).eq(path(CourseTicket::teacher)))
                )
                .whereAnd(
                    eq(path(Member::genderType), condition.genderType),
                    eq(path(Teacher::verified), condition.verified),
                    like(path(Member::nickname)(Nickname::value), condition.keyword),
                    lt(path(CourseTicket::price)(Price::value), condition.price),
                    path(Teacher::id).gt(condition.lastId ?: 0L),
                )
        }.filterNotNull()
    }

    private fun defaultPageStrategy(condition: TeacherCondition) =
        PageRequest.of(0, condition.size ?: 10)
}
