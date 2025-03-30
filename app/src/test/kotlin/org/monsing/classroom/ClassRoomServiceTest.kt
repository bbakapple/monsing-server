package org.monsing.classroom

import io.kotest.core.spec.style.FreeSpec
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlin.test.Ignore
import org.monsing.auth.jwt.LiveKitTokenManager
import org.monsing.course.LessonRepository
import org.monsing.member.MemberRepository

@Ignore
class ClassRoomServiceTest : FreeSpec({
    val memberRepository = mockk<MemberRepository>()
    val liveKitTokenManager = mockk<LiveKitTokenManager>()
    val lessonRepository = mockk<LessonRepository>()
    val sut = ClassRoomService(
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

        "수업이 진행중이지 않을 때 에러" {
        }

        "다른 선생님이 완료하려는 경우 에러" {
        }
    }
})
