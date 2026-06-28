package tech.nullexdev.cinemood.service.data.local.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import tech.nullexdev.cinemood.service.data.local.db.CineMookDatabase
import tech.nullexdev.cinemood.service.data.local.db.CineMookDatabaseConstructor
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.RoomLikedVideoDataSource
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSUserDomainMask
import tech.nullexdev.cinemood.service.data.local.repository.LikedVideoRepositoryImpl
import tech.nullexdev.cinemood.service.domain.repository.LikedVideoRepository

actual fun platformModule(): Module = module {
    single { getDatabase() }
    single { get<CineMookDatabase>().likedVideoDao() }
    single<LikedVideoDataSource> { RoomLikedVideoDataSource(get()) }
    single<LikedVideoRepository> { LikedVideoRepositoryImpl(get()) }
}

@OptIn(ExperimentalForeignApi::class)
fun getDatabase(): CineMookDatabase {
    val dbFile = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null
    )!!.path + "/cinemood.db"

    return Room.databaseBuilder<CineMookDatabase>(
        name = dbFile,
        factory = { CineMookDatabaseConstructor.initialize() }
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}