package org.monsing.classroom

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.util.Optional
import kotlin.test.Ignore
import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.course.ClassRoomRepository
import org.monsing.course.LessonRepository
import org.monsing.member.MemberRepository

@Ignore
class ClassRoomServiceTest : FreeSpec({
    val classRoomRepository = mockk<ClassRoomRepository>()
    val memberRepository = mockk<MemberRepository>()
    val liveKitTokenManager = mockk<LiveKitTokenManager>()
    val lessonRepository = mockk<LessonRepository>()
    val sut = ClassRoomService(
        classRoomRepository,
        memberRepository,
        lessonRepository,
        liveKitTokenManager
    )
    beforeTest {
        clearAllMocks()
    }

    "createClassRoom" - {
        "정상 생성" {
        }
    }

    "completeClassRoom" - {
        "정상 완료" {
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
        }
    }
})
