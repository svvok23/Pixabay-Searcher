package com.vstudio.pixabay.core.common

interface Mapper<F, T> {
    fun mapFrom(from: F): T
}