package tech.nullexdev.cinemood.service.data.local

import kotlinx.coroutines.flow.Flow
import tech.nullexdev.cinemood.service.data.local.db.CineMoodDatabase
import tech.nullexdev.cinemood.service.domain.entity.LikedVideo
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map

class SqlDelightLikedVideoDataSource(db: CineMoodDatabase) : LikedVideoDataSource {
    private val queries = db.likedVideoQueries

    override fun getLikedVideos(): Flow<List<LikedVideo>> {
        return queries.selectAll().asFlow().mapToList(Dispatchers.Default)
            .map { it.map { LikedVideo(it.id.toInt(), it.title, it.posterUrl, it.tmdbId.toInt()) } }
    }

    override suspend fun insertLikedVideo(video: LikedVideo) {
        queries.insert(video.id.toLong(), video.title, video.posterUrl, video.tmdbId.toLong())
    }

    override suspend fun deleteLikedVideo(tmdbId: Int) {
        queries.deleteById(tmdbId.toLong())
    }

    override fun getLikedVideo(tmdbId: Int): Flow<LikedVideo?> {
        return queries.selectById(tmdbId.toLong()).asFlow().mapToOneOrNull(Dispatchers.Default)
            .map { it?.let { LikedVideo(it.id.toInt(), it.title, it.posterUrl, it.tmdbId.toInt()) } }
    }
}
