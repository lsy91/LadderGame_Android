package com.quintet.laddergame.utils

/**
 * 요소가 없으면 추가, 있으면 제거하는 Map Extension Function
 *
 * @param E
 * @param element
 * @return
 */
internal fun <E> Set<E>.addOrRemove(element: E): Set<E> {
    return this.toMutableSet().apply {
        if (!add(element)) {
            remove(element)
        }
    }.toSet()
}