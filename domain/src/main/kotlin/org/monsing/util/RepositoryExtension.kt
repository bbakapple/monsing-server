package org.monsing.util

import org.springframework.core.ResolvableType
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

inline fun <reified T, ID> CrudRepository<T, ID>.findByIdOrElseThrow(id: ID, message: String? = null): T {
    val typeName = ResolvableType.forInstance(this)
        .`as`(CrudRepository::class.java)
        .resolveGenerics()[0].simpleName

    return findByIdOrNull(id) ?: throw IllegalArgumentException(message ?: "${typeName}을 찾을 수 없습니다")
}
