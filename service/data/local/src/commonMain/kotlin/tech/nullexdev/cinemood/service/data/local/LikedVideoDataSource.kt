package tech.nullexdev.cinemood.service.data.local

import kotlinx.coroutines.flow.Flow
import tech.nullexdev.cinemood.service.domain.entity.LikedVideo

interface LikedVideoDataSource {
    fun getLikedVideos(): Flow<List<LikedVideo>>
    suspend fun insertLikedVideo(video: LikedVideo)
    suspend fun deleteLikedVideo(tmdbId: Int)
    fun getLikedVideo(tmdbId: Int): Flow<LikedVideo?>
}
