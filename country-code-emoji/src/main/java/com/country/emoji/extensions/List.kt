package com.country.emoji.extensions

import java.util.Collections

fun <T, U> List<T>.intersect(uList: List<U>, filterPredicate: (T, U) -> Boolean) =
    filter { m -> uList.any { filterPredicate(m, it) } }

fun <T> List<T>.moveToFirstIf(predicate: (T) -> Boolean): List<T> {
    val default = this.find { item -> predicate(item) }
    default?.let {
        Collections.swap(this, 0, this.indexOf(it))
    }
    return this
}
