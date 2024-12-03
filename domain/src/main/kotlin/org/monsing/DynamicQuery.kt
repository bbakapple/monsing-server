package org.monsing

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.expression.Expressionable
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicatable

fun <T : Any> Jpql.eq(value: Expressionable<T>, condition: T?): Predicatable? {
    return condition?.let { value.eq(it) }
}

fun Jpql.like(value: Expressionable<String>, condition: String?): Predicatable? {
    return condition?.let { value.like("%$it%") }
}

fun <T : Comparable<T>> Jpql.lt(value: Expressionable<T>, condition: T?): Predicatable? {
    return condition?.let { value.lt(it) }
}

fun <T : Comparable<T>> Jpql.gt(value: Expressionable<T>, condition: T?): Predicatable? {
    return condition?.let { value.gt(it) }
}
