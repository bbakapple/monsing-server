//package org.monsing.record
//
//import io.kotest.assertions.throwables.shouldNotThrowAny
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.StringSpec
//import org.monsing.member.Nickname
//import org.monsing.member.OauthProviderType
//import org.monsing.member.StrongSideType
//import org.monsing.member.teacher.GenderType
//import org.monsing.member.teacher.Teacher
//
//class RecordTest : StringSpec({
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
//    "피드백을 같은 선생님에게 재요청할 수 없다" {
//        val record = Record(id = 1, title = "title", studentId = 1, fileKey = "key")
//        record.requestFeedback(testTeacher)
//
//        shouldThrow<IllegalArgumentException> {
//            record.requestFeedback(testTeacher)
//        }
//    }
//
//    "녹음 제목을 30자로 수정할 수 있다" {
//        val record = Record(title = "title", studentId = 1, fileKey = "key")
//
//        shouldNotThrowAny {
//
//            record.updateTitle("1".repeat(30))
//        }
//    }
//
//    "녹음 제목을 30자 초과로 수정할 수 없다" {
//        val record = Record(title = "title", studentId = 1, fileKey = "key")
//
//        shouldThrow<IllegalArgumentException> {
//            record.updateTitle("3".repeat(31))
//        }
//    }
//
//    "녹음 제목을 공백으로 수정할 수 없다" {
//        val record = Record(title = "title", studentId = 1, fileKey = "key")
//
//        shouldThrow<IllegalArgumentException> {
//            record.updateTitle("  ")
//        }
//    }
//})
