package com.vstudio.pixabay.core.common

interface Mapper<F, T> {
    fun mapFrom(from: F): T
}

inline fun <F, T> Mapper<F, T>.mapTo(from: F): T = mapFrom(from)