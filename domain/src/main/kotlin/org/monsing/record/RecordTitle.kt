package org.monsing.record

import jakarta.persistence.Column

class RecordTitle(
    @Column(nullable = false, name = "title")
    val value: String
) {
    init {
        require(value.isNotBlank()) { "Title must not be blank" }
        require(value.length <= 30) { "Title must not exceed 30 characters" }
        require(isValidPattern(value)) { "illegal pattern" }
    }

    private fun isValidPattern(value: String): Boolean {
        val pattern = Regex("^[가-힣a-zA-Z0-9._\\-()]*$")
        return pattern.matches(value)
    }
}
