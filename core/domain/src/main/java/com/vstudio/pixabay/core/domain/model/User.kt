package com.vstudio.pixabay.core.domain.model

data class User(
    val id: Long = 0,
    val name: String = "",
    val avatarUrl: String = "",
) {
    val profileUrl = "https://pixabay.com/users/$name-$id"
}