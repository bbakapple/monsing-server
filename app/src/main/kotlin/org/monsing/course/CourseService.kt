package org.monsing.course

import org.monsing.member.MemberRepository
import org.monsing.util.findByIdOrElseThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun createCourse(
        id: Long,
        name: String,
        description: String,
        curriculum: String,
        duration: Int,
        price: Int,
        minimumLessonCount: Int,
        lessonSchedules: List<LessonSchedule>
    ) {

        val teacher = memberRepository.findTeacherById(id)

        val lessons = lessonSchedules.map {
            Lesson(
                lessonSchedule = it
            )
        }

        val course = Course(
            courseOverview = CourseOverview(
                name = name,
                description = description,
                curriculum = curriculum
            ),
            teacherId = requireNotNull(teacher.id),
            duration = CourseDuration(duration),
            pricePerLesson = CoursePricePerLesson(price),
            minimumLessonCount = CourseMinimumLessonCount(minimumLessonCount),
            lessons = lessons
        )

        courseRepository.save(course)
    }

    @Transactional
    fun updateCourse(
        memberId: Long,
        courseId: Long,
        name: String?,
        description: String?,
        curriculum: String?,
        duration: Int?,
        price: Int?,
        minimumLessonCount: Int?
    ) {

        val teacher = memberRepository.findTeacherById(memberId)

        val course = courseRepository.findByIdOrElseThrow(courseId)

        require(course.teacherId == requireNotNull(teacher.id)) {
            "Teacher is not the owner of the course"
        }

        course.update(
            name,
            description,
            curriculum,
            duration,
            price,
            minimumLessonCount
        )
    }

    @Transactional
    fun registerLesson(
        id: Long,
        courseId: Long,
        lessonId: Long,
        lessonCount: Int
    ) {
        val student = memberRepository.findStudentById(id)

        val course = courseRepository.findByIdOrElseThrow(courseId)

        course.registerLesson(requireNotNull(student.id), lessonId, lessonCount)
    }

    @Transactional(readOnly = true)
    fun getCoursesByTeacherId(teacherId: Long): List<Course> {
        return courseRepository.findAllByTeacherId(teacherId)
    }

    @Transactional(readOnly = true)
    fun getLessonsByCourseId(id: Long): List<Lesson> {
        return courseRepository.findByIdOrElseThrow(id).lessons
    }
}
