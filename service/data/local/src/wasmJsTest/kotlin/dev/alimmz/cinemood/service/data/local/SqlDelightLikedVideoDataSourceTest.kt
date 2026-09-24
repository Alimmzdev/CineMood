package dev.alimmz.cinemood.service.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver
import dev.alimmz.cinemood.service.domain.entity.LikedVideo
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SqlDelightLikedVideoDataSourceTest {
    @Test
    fun concurrentFirstReadsAwaitOneSchemaInitialization() = runTest {
        var driver: SqlDriver? = null
        var driverCount = 0
        val source = SqlDelightLikedVideoDataSource {
            driverCount++
            createDefaultWebWorkerDriver().also { driver = it }
        }
        try {
            assertEquals(0, driverCount)
            val all = async { source.getLikedVideos().first() }
            val one = async { source.getLikedVideo(42).first() }

            assertTrue(all.await().isEmpty())
            assertNull(one.await())
            assertEquals(1, driverCount)
        } finally {
            driver?.close()
        }
    }

    @Test
    fun favoritesKeepDistinctMoviesAndEmitChanges() = runTest {
        val driver = createDefaultWebWorkerDriver()
        val source = SqlDelightLikedVideoDataSource { driver }
        val first = LikedVideo(id = 0, title = "First", posterUrl = "first.jpg", tmdbId = 42)
        val second = LikedVideo(id = 0, title = "Second", posterUrl = "second.jpg", tmdbId = 99)
        try {
            assertTrue(source.getLikedVideos().first().isEmpty())
            val updatedFavorites = async(start = CoroutineStart.UNDISPATCHED) {
                source.getLikedVideos().first { it.size == 2 }
            }
            source.insertLikedVideo(first)
            source.insertLikedVideo(second)
            assertEquals(setOf(42, 99), updatedFavorites.await().map { it.tmdbId }.toSet())

            source.insertLikedVideo(first.copy(title = "Updated"))
            assertEquals(2, source.getLikedVideos().first().size)
            assertEquals("Updated", source.getLikedVideo(42).first()?.title)

            source.deleteLikedVideo(42)
            assertNull(source.getLikedVideo(42).first())
            assertEquals(listOf(99), source.getLikedVideos().first().map { it.tmdbId })
        } finally {
            driver.close()
        }
    }
}
