package com.alimmzdev.cinemood_kmp.service.data.iranianmoviesapi.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetadataDto(
    val current_page: Int,
    val per_page: Int,
    val page_count: Int,
    val total_count: Int
)