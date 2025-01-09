package org.monsing.record

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class RecordTest : StringSpec({

    "피드백을 같은 선생님에게 재요청할 수 없다" {
        val record = Record("title", 1, "key")
        record.requestFeedback(1)

        shouldThrow<IllegalArgumentException> {
            record.requestFeedback(1)
        }
    }

    "녹음 제목을 30자로 설정할 수 있다" {
        shouldNotThrowAny {
            Record("1".repeat(30), 1, "key")
        }
    }

    "녹음 제목을 30자 초과로 설정할 수 없다" {
        shouldThrow<IllegalArgumentException> {
            Record("2".repeat(31), 1, "key")
        }
    }
})
