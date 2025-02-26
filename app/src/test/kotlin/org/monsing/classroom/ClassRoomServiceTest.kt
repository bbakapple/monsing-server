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
import org.monsing.course.ClassRoom
import org.monsing.course.ClassRoomRepository
import org.monsing.member.MemberRepository
import org.monsing.member.Student
import org.monsing.member.teacher.Teacher

class ClassRoomServiceTest : FreeSpec({
    val classRoomRepository = mockk<ClassRoomRepository>()
    val memberRepository = mockk<MemberRepository>()
    val liveKitTokenManager = mockk<LiveKitTokenManager>()
    val sut = ClassRoomService(
        classRoomRepository,
        memberRepository,
        liveKitTokenManager
    )
    beforeTest {
        clearAllMocks()
    }

    "createClassRoom" - {
        "정상 생성" {
            // given
            val teacherId = 1L
            val studentId = 2L
            every { memberRepository.findTeacherById(teacherId) } returns mockk<Teacher>(relaxed = true) {
                every { id } returns teacherId
            }
            every { memberRepository.findStudentById(studentId) } returns mockk<Student>(relaxed = true) {
                every { id } returns studentId
            }
            every { classRoomRepository.save(any()) } returns ClassRoom(
                teacher = mockk(),
                student = mockk(),
                status = mockk()
            )

            // when
            val result = sut.createClassRoom(teacherId, studentId)

            // then
            verify(exactly = 1) { classRoomRepository.save(any()) }
        }
    }

    "retrieveClassRooms" - {
        "선생님 역할로 조회" {
            // given
            val teacherId = 1L
            val classRooms = listOf(mockk<ClassRoom>(relaxed = true))
            every { classRoomRepository.findByStudentIdOrTeacherId(teacherId, teacherId) } returns classRooms

            // when
            val result = sut.retrieveClassRooms(teacherId)

            // then
            result shouldBe classRooms
        }

        "학생 역할로 조회" {
            // given
            val studentId = 1L
            val classRooms = listOf(mockk<ClassRoom>(relaxed = true))
            every { classRoomRepository.findByStudentIdOrTeacherId(studentId, studentId) } returns classRooms

            // when
            val result = sut.retrieveClassRooms(studentId)

            // then
            result shouldBe classRooms
        }
    }

    "completeClassRoom" - {
        "정상 완료" {
            // given
            val teacherId = 1L
            val classRoomId = 1L
            val classRoom = mockk<ClassRoom>(relaxed = true) {
                every { teacher.id } returns teacherId
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
