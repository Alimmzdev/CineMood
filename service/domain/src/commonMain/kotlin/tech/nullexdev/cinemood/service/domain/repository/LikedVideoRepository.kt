package tech.nullexdev.cinemood.service.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.nullexdev.cinemood.service.domain.entity.LikedVideo

interface LikedVideoRepository {
    fun getLikedVideos(): Flow<List<LikedVideo>>
    suspend fun insertLikedVideo(video: LikedVideo)
    suspend fun deleteLikedVideo(tmdbId: Int)
    fun getLikedVideo(tmdbId: Int): Flow<LikedVideo?>
}
