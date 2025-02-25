package org.monsing.course

import org.monsing.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
            ?: throw IllegalArgumentException("Teacher not found")

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
            ?: throw IllegalArgumentException("Teacher not found")

        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw IllegalArgumentException("Course not found")

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
            ?: throw IllegalArgumentException("Student not found")

        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw IllegalArgumentException("Course not found")

        require(course.hasLesserLessonCount(lessonCount)) {
            "register should be greater than minimum lesson count"
        }

        val lesson = course.findLessonById(lessonId)

        lesson.register(requireNotNull(student.id), lessonCount)
    }
}
