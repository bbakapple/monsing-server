//package org.monsing.record
//
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.StringSpec
//import io.kotest.matchers.shouldBe
//import org.monsing.member.teacher.Teacher
//import org.monsing.record.feedback.FeedbackItem
//import org.monsing.record.feedback.FeedbackTicket
//
//class FeedBackTicketTest : StringSpec({
//
//    "피드백 요청시 수량이 1감소한다" {
//        val ticket = FeedbackTicket(1, 1, 20)
//FeedbackTicket(
//    feedbackItem = FeedbackItem(Teacher(1))
//)
//        ticket.decreaseAmount(1)
//
//        ticket.amount shouldBe 19
//    }
//
//    "피드백 요청시 수량이 0이면 예외가 발생한다" {
//        val ticket = FeedbackTicket(1, 1, 0)
//
//        shouldThrow<IllegalArgumentException> {
//            ticket.decreaseAmount(1)
//        }
//    }
//
//    "피드백 요청시 수량이 음수이면 예외가 발생한다" {
//        val ticket = FeedbackTicket(1, 1, -1)
//
//        shouldThrow<IllegalArgumentException> {
//            ticket.decreaseAmount(1)
//        }
//    }
//})
