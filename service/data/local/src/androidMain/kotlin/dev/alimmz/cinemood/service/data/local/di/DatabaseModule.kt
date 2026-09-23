package dev.alimmz.cinemood.service.data.local.di

import androidx.room.Room
import org.koin.dsl.module
import dev.alimmz.cinemood.service.data.local.db.CineMookDatabase
import dev.alimmz.cinemood.service.data.local.LikedVideoDataSource
import dev.alimmz.cinemood.service.data.local.RoomLikedVideoDataSource

val databaseModule = module {
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
