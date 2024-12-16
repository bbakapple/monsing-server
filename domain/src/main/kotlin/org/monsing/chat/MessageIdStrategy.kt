package org.monsing.chat

interface MessageIdStrategy {

    fun generateId(message: Message)
}
