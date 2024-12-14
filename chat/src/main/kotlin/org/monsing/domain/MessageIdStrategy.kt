package org.monsing.domain

interface MessageIdStrategy {

    fun generateId(message: Message)
}
