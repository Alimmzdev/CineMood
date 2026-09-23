package dev.alimmz.cinemood.service.data.local

import kotlinx.coroutines.flow.Flow
import dev.alimmz.cinemood.service.domain.entity.LikedVideo

interface LikedVideoDataSource {
    fun getLikedVideos(): Flow<List<LikedVideo>>
    suspend fun insertLikedVideo(video: LikedVideo)
    suspend fun deleteLikedVideo(tmdbId: Int)
    fun getLikedVideo(tmdbId: Int): Flow<LikedVideo?>
}
