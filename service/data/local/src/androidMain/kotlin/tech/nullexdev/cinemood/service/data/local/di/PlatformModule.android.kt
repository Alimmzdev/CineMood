package tech.nullexdev.cinemood.service.data.local.di

import org.koin.core.module.Module
import org.koin.dsl.module
import androidx.room.Room
import tech.nullexdev.cinemood.service.data.local.db.CineMookDatabase
import tech.nullexdev.cinemood.service.data.local.LikedVideoDataSource
import tech.nullexdev.cinemood.service.data.local.RoomLikedVideoDataSource

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
