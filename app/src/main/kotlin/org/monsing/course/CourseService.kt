package org.monsing.course

import org.monsing.auth.jwt.Role
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.TeacherRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val teacherRepository: TeacherRepository,
    private val studentRepository: StudentRepository
) {

    @Transactional
    fun createCourse(
        id: Long,
        role: Role,
        name: String,
        description: String,
        curriculum: String,
        duration: Int,
        price: Int,
        minimumLessonCount: Int,
        lessonSchedules: List<LessonSchedule>
    ) {
        val teacherId = teacherRepository.findByMemberId(id)
            ?.id
            ?: throw IllegalArgumentException("Teacher not found")

        require(role == Role.TEACHER) {
            "Role is invalid"
        }

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
            teacherId = teacherId,
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
        val teacherId = teacherRepository.findByMemberId(memberId)
            ?.id
            ?: throw IllegalArgumentException("Teacher not found")

        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw IllegalArgumentException("Course not found")

        require(course.teacherId == teacherId) {
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
        role: Role,
        courseId: Long,
        lessonId: Long,
        lessonCount: Int
    ) {
        val student = studentRepository.findByMemberId(id)?.id
            ?: throw IllegalArgumentException("Student not found")

        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw IllegalArgumentException("Course not found")

        require(course.hasLesserLessonCount(lessonCount)) {
            "register should be greater than minimum lesson count"
        }

        val lesson = course.findLessonById(lessonId)

        lesson.register(student, lessonCount)
    }
}
