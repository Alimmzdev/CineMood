package dev.alimmz.cinemood.service.domain.entity

data class LikedVideo(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val tmdbId: Int
)
