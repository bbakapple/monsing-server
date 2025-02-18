package org.monsing.classroom

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.monsing.auth.jwt.Role
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.Student
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.Teacher
import org.monsing.member.teacher.TeacherRepository
import java.util.Optional

class ClassRoomServiceTest : FreeSpec({
    val classRoomRepository = mockk<ClassRoomRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val studentRepository = mockk<StudentRepository>()
    val sut = ClassRoomService(
        classRoomRepository,
        teacherRepository,
        studentRepository
    )
    beforeTest {
        clearAllMocks()
    }

    "createClassRoom" - {
        "정상 생성" {
            // given
            val teacherId = 1L
            val role = Role.TEACHER
            val studentId = 1
            every { teacherRepository.findById(teacherId) } returns Optional.of(mockk<Teacher>(relaxed = true) {
                every { id } returns teacherId
            })
            every { studentRepository.findById(studentId.toLong()) } returns Optional.of(mockk<Student>(relaxed = true) {
                every { id } returns studentId.toLong()
            })
            every { classRoomRepository.save(any()) } returns ClassRoom(
                teacher = mockk(),
                student = mockk(),
                status = mockk()
            )

            // when
            val result = sut.createClassRoom(teacherId, role, studentId)

            // then
            every { classRoomRepository.save(any()) }
        }
        "선생이 아닌 경우 에러" {
            // given
            val teacherId = 1L
            val role = Role.STUDENT
            val studentId = 1

            // when
            val exception = shouldThrow<IllegalStateException> {
                sut.createClassRoom(teacherId, role, studentId)
            }
        }
        "선생이 없는 경우 에러" {
            // given
            val teacherId = 1L
            val role = Role.TEACHER
            val studentId = 1
            every { teacherRepository.findById(teacherId) } returns Optional.empty()

            // when
            val exception = shouldThrow<IllegalArgumentException> {
                sut.createClassRoom(teacherId, role, studentId)
            }
        }

        "학생이 없는 경우 에러" {
            // given
            val teacherId = 1L
            val role = Role.TEACHER
            val studentId = 1
            every { teacherRepository.findById(teacherId) } returns Optional.of(mockk())
            every { studentRepository.findById(studentId.toLong()) } returns Optional.empty()

            // when
            val exception = shouldThrow<IllegalArgumentException> {
                sut.createClassRoom(teacherId, role, studentId)
            }
        }
    }
    "retrieveClassRooms" - {
        "선생님 역할로 조회" {
            // given
            val teacherId = 1L
            val role = Role.TEACHER
            val classRooms = listOf(mockk<ClassRoom>(relaxed = true))
            every { classRoomRepository.findByTeacherId(teacherId) } returns classRooms

            // when
            val result = sut.retrieveClassRooms(teacherId, role)

            // then
            result shouldBe classRooms
        }

        "학생 역할로 조회" {
            // given
            val studentId = 1L
            val role = Role.STUDENT
            val classRooms = listOf(mockk<ClassRoom>(relaxed = true))
            every { classRoomRepository.findByStudentId(studentId) } returns classRooms

            // when
            val result = sut.retrieveClassRooms(studentId, role)

            // then
            result shouldBe classRooms
        }

        "역할이 NONE인 경우 에러" {
            // given
            val id = 1L
            val role = Role.NONE

            // when
            val exception = shouldThrow<IllegalArgumentException> {
                sut.retrieveClassRooms(id, role)
            }

            // then
            exception.message shouldBe "Role is NONE"
        }
    }
})
