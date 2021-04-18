package com.eneskayiklik.yellowtaxi.util.extensions

inline fun <T, R: Comparable<R>> Iterable<T>.maxByIndexOf(selector: (T) -> R): Int {
    var result = 0
    var max: T = this.first()
    this.forEachIndexed { i, d ->
        if (selector(max) < selector(d)) {
            max = d
            result = i
        }
    }
    return result
}