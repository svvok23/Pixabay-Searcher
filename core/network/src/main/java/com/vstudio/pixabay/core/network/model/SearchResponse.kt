package com.vstudio.pixabay.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val hits: List<HitDto> = emptyList(),
    val total: Int = 0,
    val totalHits: Int = 0,
)