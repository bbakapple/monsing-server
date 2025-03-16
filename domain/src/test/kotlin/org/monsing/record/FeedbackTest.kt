//package org.monsing.record
//
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.StringSpec
//import org.monsing.member.Nickname
//import org.monsing.member.OauthProviderType
//import org.monsing.member.StrongSideType
//import org.monsing.member.teacher.GenderType
//import org.monsing.member.teacher.Teacher
//import org.monsing.record.feedback.Feedback
//import org.monsing.record.feedback.FeedbackStatus
//
//class FeedbackTest : StringSpec({
//    val testTeacher = Teacher(
//        id = 1L,
//        identifier = "testIdentifier",
//        oauthProviderType = OauthProviderType.GOOGLE,
//        nickname = Nickname("testNickname"),
//        summary = "Test summary",
//        strongSideType = StrongSideType.FIX,
//        description = "Test description",
//        forStudent = "Test for student",
//        verified = true,
//        profileImage = "http://example.com/profile.jpg",
//        genderType = GenderType.MALE,
//        portfolios = mutableListOf(),
//        careers = mutableListOf()
//    )
//
//    "피드백을 공백으로 작성할 수 없다" {
//        val feedback = Feedback(recordId = 1, teacher = testTeacher)
//
//        shouldThrow<IllegalArgumentException> {
//            feedback.writeFeedback("  ")
//        }
//    }
//
//    "피드백이 완료되어 있으면 작성할 수 없다" {
//        val feedback = Feedback(
//            testTeacher,
//            1,
//            "내용",
//            FeedbackStatus.COMPLETED
//        )
//
//        shouldThrow<IllegalArgumentException> {
//            feedback.writeFeedback("내용")
//        }
//    }
//
//    "피드백이 3000자를 초과하면 작성할 수 없다" {
//        val feedback = Feedback(testTeacher, 1)
//
//        shouldThrow<IllegalArgumentException> {
//            feedback.writeFeedback("a".repeat(3001))
//        }
//    }
//
//    "피드백이 3000자 이하면 작성할 수 있다" {
//        val feedback = Feedback(testTeacher, 1)
//
//        feedback.writeFeedback("a".repeat(3000))
//    }
//})
