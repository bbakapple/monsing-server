package org.monsing.record

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class RecordTitleTest : StringSpec({


    "녹음 제목을 30자로 만들 수 있다" {
        shouldNotThrowAny {
            RecordTitle("1".repeat(30))
        }
    }

    "녹음 제목을 30자 초과로 만들 수 없다" {
        shouldThrow<IllegalArgumentException> {
            RecordTitle("2".repeat(31))
        }
    }

    "녹음 제목을 공백으로 만들 수 없다" {
        shouldThrow<IllegalArgumentException> {
            Record("  ", 1, "key")
        }
    }

    "녹음 제목에 허용된 문자열을 검증한다" {
        shouldNotThrowAny {
            RecordTitle("가힣긔긁긝azAZ09._-()")
        }
    }

    "녹음 제목에 허용되지 않은 문자를 사용하면 예외가 발생한다" {
        val invalidPattern = listOf("!", "@", "#", "$", "?")

        invalidPattern.forEach {
            shouldThrow<IllegalArgumentException> {
                RecordTitle(it)
            }
        }
    }
})
