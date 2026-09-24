package dev.alimmz.cinemood.service.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import dev.alimmz.cinemood.service.data.local.db.CineMoodDatabase
import dev.alimmz.cinemood.service.domain.entity.LikedVideo

class SqlDelightLikedVideoDataSource(
    private val createDriver: () -> SqlDriver = ::createDefaultWebWorkerDriver,
) : LikedVideoDataSource {
    private val initializationMutex = Mutex()
    private var database: CineMoodDatabase? = null

    // Koin constructs this synchronously; the first operation awaits schema creation.
    private suspend fun queries() = initializationMutex.withLock {
        val db = database ?: run {
            val driver = createDriver()
            try {
                CineMoodDatabase.Schema.create(driver).await()
                CineMoodDatabase(driver).also { database = it }
            } catch (error: Throwable) {
                driver.close()
                throw error
            }
        }
        db.likedVideoQueries
    }

    override fun getLikedVideos(): Flow<List<LikedVideo>> {
        return flow {
            emitAll(
                queries().selectAll().asFlow().mapToList(Dispatchers.Default)
                    .map { list -> list.map { LikedVideo(it.id.toInt(), it.title, it.posterUrl, it.tmdbId.toInt()) } }
            )
        }
    }

    override suspend fun insertLikedVideo(video: LikedVideo) {
        // New favorites all have id = 0; use the movie ID to keep distinct movies.
        queries().insert(video.tmdbId.toLong(), video.title, video.posterUrl, video.tmdbId.toLong())
    }

    override suspend fun deleteLikedVideo(tmdbId: Int) {
        queries().deleteById(tmdbId.toLong())
    }

    override fun getLikedVideo(tmdbId: Int): Flow<LikedVideo?> {
        return flow {
            emitAll(
                queries().selectById(tmdbId.toLong()).asFlow().mapToOneOrNull(Dispatchers.Default)
                    .map { it?.let { LikedVideo(it.id.toInt(), it.title, it.posterUrl, it.tmdbId.toInt()) } }
            )
        }
    }
}
