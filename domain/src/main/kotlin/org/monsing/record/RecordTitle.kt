package org.monsing.record

import jakarta.persistence.Column

private const val MAXIMUM_TITLE_LENGTH = 30

class RecordTitle(
    @Column(nullable = false, name = "title")
    val value: String
) {
    init {
        require(value.isNotBlank()) { "Title must not be blank" }
        require(value.length <= MAXIMUM_TITLE_LENGTH) {
            "Title must not exceed $MAXIMUM_TITLE_LENGTH characters"
        }
        require(value.matches(PATTERN)) { "illegal pattern" }
    }

    companion object {
        private val PATTERN = Regex("^[가-힣a-zA-Z0-9._\\-()]*$")
    }
}
