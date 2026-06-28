package tech.nullexdev.cinemood.service.data.local.db.entity

import tech.nullexdev.cinemood.service.domain.entity.LikedVideo

fun LikedVideoEntity.toDomain() = LikedVideo(
    id = id,
    title = title,
    posterUrl = posterUrl,
    tmdbId = tmdbId
)

fun LikedVideo.toEntity() = LikedVideoEntity(
    id = id,
    title = title,
    posterUrl = posterUrl,
    tmdbId = tmdbId
)
