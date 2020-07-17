package com.devpraskov.android_ext

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long?): Long {
    var sum = 0L
    for (element in this) {
        sum += selector(element) ?: 0
    }
    return sum
}