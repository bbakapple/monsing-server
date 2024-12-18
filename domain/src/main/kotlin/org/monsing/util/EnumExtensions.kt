package org.monsing.util

inline fun <reified T : Enum<T>> enumValueOrNull(name: String?): T? =
    enumValues<T>().find { it.name == name }
