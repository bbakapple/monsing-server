package org.monsing.util

import org.springframework.core.ResolvableType
import org.springframework.data.repository.CrudRepository

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrElseThrow(id: ID, message: String? = null): T {
    return findById(id!!).orElseThrow {
        val typeName = ResolvableType.forInstance(this)
            .`as`(CrudRepository::class.java)
            .resolveGenerics()[0].simpleName
        IllegalArgumentException(message ?: "${typeName}을 찾을 수 없습니다")
    }
}
