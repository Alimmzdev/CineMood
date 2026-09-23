package dev.alimmz.cinemood.service.data.local.di

import org.koin.core.module.Module
import org.koin.dsl.module
import androidx.room.Room
import dev.alimmz.cinemood.service.data.local.db.CineMookDatabase
import dev.alimmz.cinemood.service.data.local.LikedVideoDataSource
import dev.alimmz.cinemood.service.data.local.RoomLikedVideoDataSource

actual fun platformModule(): Module = module {
    single {
        Room.databaseBuilder(
            get(),
            CineMookDatabase::class.java,
            "cinemook.db"
        ).build()
    }

    single { get<CineMookDatabase>().likedVideoDao() }
    single<LikedVideoDataSource> { RoomLikedVideoDataSource(get()) }
}
