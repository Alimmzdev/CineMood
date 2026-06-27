package tech.nullexdev.cinemood.service.data.local.repository

import kotlinx.coroutines.flow.Flow
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.domain.entity.LikedVideo
import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

class LikedVideoRepositoryImpl(private val dataSource: LikedVideoDataSource) : LikedVideoRepository {
    override fun getLikedVideos(): Flow<List<LikedVideo>> {
        return dataSource.getLikedVideos()
    }

    override suspend fun insertLikedVideo(video: LikedVideo) {
        dataSource.insertLikedVideo(video)
    }

    override suspend fun deleteLikedVideo(tmdbId: Int) {
        dataSource.deleteLikedVideo(tmdbId)
    }

    override fun getLikedVideo(tmdbId: Int): Flow<LikedVideo?> {
        return dataSource.getLikedVideo(tmdbId)
    }
}
