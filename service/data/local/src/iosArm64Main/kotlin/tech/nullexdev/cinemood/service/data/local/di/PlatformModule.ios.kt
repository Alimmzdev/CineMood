package tech.nullexdev.cinemood.service.data.local.di

import androidx.room.Room
import org.koin.core.module.Module
import org.koin.dsl.module
import tech.nullexdev.cinemood.service.data.local.db.CineMookDatabase
import tech.nullexdev.cinemood.service.data.local.db.CineMookDatabaseConstructor
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.RoomLikedVideoDataSource
import platform.Foundation.NSHomeDirectory

actual fun platformModule(): Module = module {
    single { getDatabase() }
    single { get<CineMookDatabase>().likedVideoDao() }
    single<LikedVideoDataSource> { RoomLikedVideoDataSource(get()) }
}

fun getDatabase(): CineMookDatabase {
    val dbFile = NSHomeDirectory() + "/cinemook.db"
    return Room.databaseBuilder<CineMookDatabase>(
        name = dbFile,
        factory = { CineMookDatabaseConstructor.initialize() }
    ).build()
}
