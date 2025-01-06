package org.monsing.record

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class RecordTest : StringSpec({

    "피드백을 같은 선생님에게 재요청할 수 없다" {
        val record = Record(1, "key")
        record.requestFeedback(1)

        shouldThrow<IllegalArgumentException> {
            record.requestFeedback(1)
        }
    }
})
