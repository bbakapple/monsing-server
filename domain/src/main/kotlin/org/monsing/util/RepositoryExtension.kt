package org.monsing.util

import org.springframework.data.repository.CrudRepository

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrElseThrow(id: ID, message: String? = null): T {
    return findById(id!!).orElseThrow {
        IllegalArgumentException(message ?: "${T::class.javaClass.simpleName}을 찾을 수 없습니다")
    }
}
