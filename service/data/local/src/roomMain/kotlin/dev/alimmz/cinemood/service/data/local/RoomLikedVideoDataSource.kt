package dev.alimmz.cinemood.service.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dev.alimmz.cinemood.service.data.local.db.LikedVideoDao
import dev.alimmz.cinemood.service.data.local.db.entity.toDomain
import dev.alimmz.cinemood.service.data.local.db.entity.toEntity
import dev.alimmz.cinemood.service.domain.entity.LikedVideo

class RoomLikedVideoDataSource(private val likedVideoDao: LikedVideoDao) : LikedVideoDataSource {
    override fun getLikedVideos(): Flow<List<LikedVideo>> {
        return likedVideoDao.getLikedVideos().map { it.map { it.toDomain() } }
    }

    override suspend fun insertLikedVideo(video: LikedVideo) {
        likedVideoDao.insertLikedVideo(video.toEntity())
    }

    override suspend fun deleteLikedVideo(tmdbId: Int) {
        likedVideoDao.deleteLikedVideo(tmdbId)
    }

    override fun getLikedVideo(tmdbId: Int): Flow<LikedVideo?> {
        return likedVideoDao.getLikedVideo(tmdbId).map { it?.toDomain() }
    }
}
