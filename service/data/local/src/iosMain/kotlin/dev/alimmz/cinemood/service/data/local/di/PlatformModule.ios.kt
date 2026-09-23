package dev.alimmz.cinemood.service.data.local.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import dev.alimmz.cinemood.service.data.local.LikedVideoDataSource
import dev.alimmz.cinemood.service.data.local.RoomLikedVideoDataSource
import dev.alimmz.cinemood.service.data.local.db.CineMookDatabase
import dev.alimmz.cinemood.service.data.local.db.CineMookDatabaseConstructor

@OptIn(ExperimentalForeignApi::class)
private fun getDatabase(): CineMookDatabase {
    val dbFile = NSFileManager.defaultManager
        .URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )!!
        .path + "/cinemood.db"

    return Room.databaseBuilder<CineMookDatabase>(
        name = dbFile,
        factory = {
            CineMookDatabaseConstructor.initialize()
        }
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

actual fun platformModule(): Module = module {
    single { getDatabase() }
    single { get<CineMookDatabase>().likedVideoDao() }
    single<LikedVideoDataSource> {
        RoomLikedVideoDataSource(get())
    }
}