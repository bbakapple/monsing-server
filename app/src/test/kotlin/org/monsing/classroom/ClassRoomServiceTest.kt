package org.monsing.classroom

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional
import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.auth.jwt.Role
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.Student
import org.monsing.member.StudentRepository
import org.monsing.member.teacher.Teacher
import org.monsing.member.teacher.TeacherRepository

class ClassRoomServiceTest : FreeSpec({
    val classRoomRepository = mockk<ClassRoomRepository>()
    val teacherRepository = mockk<TeacherRepository>()
    val studentRepository = mockk<StudentRepository>()
    val liveKitTokenManager = mockk<LiveKitTokenManager>()
    val sut = ClassRoomService(
        classRoomRepository,
        teacherRepository,
        studentRepository,
        liveKitTokenManager
    )
    beforeTest {
        clearAllMocks()
    }

    "createClassRoom" - {
        "정상 생성" {
            // given
            val teacherId = 1L
            val role = Role.TEACHER
            val studentId = 1L
            every { teacherRepository.findByMemberId(teacherId) } returns mockk<Teacher>(relaxed = true) {
                every { id } returns teacherId
            }
            every { studentRepository.findById(studentId) } returns Optional.of(mockk<Student>(relaxed = true) {
                every { id } returns studentId
            })
            every { classRoomRepository.save(any()) } returns ClassRoom(
                teacher = mockk(),
                student = mockk(),
                status = mockk()
            )

            // when
            val result = sut.createClassRoom(teacherId, role, studentId)

            // then
            verify(exactly = 1) { classRoomRepository.save(any()) }
        }
        "선생이 아닌 경우 에러" {
            // given
            val teacherId = 1L
            val role = Role.STUDENT
            val studentId = 1L

            // when
            val exception = shouldThrow<IllegalStateException> {
                sut.createClassRoom(teacherId, role, studentId)
            }
        }
        "선생이 없는 경우 에러" {
            // given
            val teacherId = 1L
            val role = Role.TEACHER
            val studentId = 1L
            every { teacherRepository.findByMemberId(teacherId) } returns null

            // when
            val exception = shouldThrow<IllegalArgumentException> {
                sut.createClassRoom(teacherId, role, studentId)
            }
        }

        "학생이 없는 경우 에러" {
            // given
            val teacherId = 1L
            val role = Role.TEACHER
            val studentId = 1L
            every { teacherRepository.findByMemberId(teacherId) } returns mockk()
            every { studentRepository.findById(studentId) } returns Optional.empty()

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

    "completeClassRoom" - {
        "정상 완료" {
            // given
            val teacherId = 1L
            val classRoomId = 1L
            val classRoom = mockk<ClassRoom>(relaxed = true) {
                every { teacher.memberId } returns teacherId
            }
            every { classRoomRepository.findById(classRoomId) } returns Optional.of(classRoom)

            // when
            sut.completeClassRoom(teacherId, classRoomId)

            // then
            verify { classRoom.complete() }
        }

        "클래스룸이 없는 경우 에러" {
            // given
            val teacherId = 1L
            val classRoomId = 1L
            every { classRoomRepository.findById(classRoomId) } returns Optional.empty()

            // when
            val exception = shouldThrow<IllegalArgumentException> {
                sut.completeClassRoom(teacherId, classRoomId)
            }

            // then
            exception.message shouldBe "ClassRoom을 찾을 수 없습니다"
        }

        "다른 선생님이 완료하려는 경우 에러" {
            // given
            val teacherId = 1L
            val classRoomId = 1L
            val classRoom = mockk<ClassRoom>(relaxed = true) {
                every { teacher.id } returns 2L
            }
            every { classRoomRepository.findById(classRoomId) } returns Optional.of(classRoom)

            // when
            val exception = shouldThrow<IllegalStateException> {
                sut.completeClassRoom(teacherId, classRoomId)
            }

            // then
            exception.message shouldBe "Only teacher can complete a class room"
        }
    }
})
